package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dao.ServerMapper;
import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Server;
import com.tute.sujia.entity.Task;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.utils.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CapacityTaskScheduler extends TaskScheduler {
    private static LinkedBlockingQueue<Task> little = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Task> middle = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Task> large = new LinkedBlockingQueue<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FIFOTaskScheduler.class);
    private static TreeSet<String> addressSet;
    private static Map<String, TreeSet<String>> map = new HashMap<>();

    static {
        map.put("LITTLE", new TreeSet<>());
        map.put("MIDDLE", new TreeSet<>());
        map.put("LARGE", new TreeSet<>());
    }

    @Autowired
    ServerMapper serverMapper;

    @Override
    public void setAddress(TreeSet<String> addressSet) {
        // 计算比例
        double totalCapacity = 0.0;
        for (String s : addressSet) {
            Server server = serverMapper.getServerByAddress(s);
            totalCapacity += server.getServer_memory();
        }

        for (String s : addressSet) {
            Server server = serverMapper.getServerByAddress(s);
            double base = totalCapacity / 3;
            if (server.getServer_memory() / base > 0.7) {
                TreeSet<String> tLarge = map.get("LARGE");
                tLarge.add(server.getServer_add());
            } else if (server.getServer_memory() / base > 0.3) {
                TreeSet<String> tMiddle = map.get("MIDDLE");
                tMiddle.add(server.getServer_add());
            } else {
                TreeSet<String> tLittle = map.get("LITTLE");
                tLittle.add(server.getServer_add());
            }
        }
    }

    @Override
    public String dispatch(Task task) {
        if (task.getCapacity() <= 3) {
            deal(little, task);
        } else if (task.getCapacity() >= 7) {
            deal(large, task);
        } else {
            deal(middle, task);
        }
        return "CAPACITY";
    }

    @Override
    public void exec() {
        while (true) {
            try {
                Task tLittle = little.poll();
                Task tMiddle = middle.poll();
                Task tLarge = large.poll();
                exec(tLittle, "LITTLE");
                exec(tMiddle, "MIDDLE");
                exec(tLarge, "LARGE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void exec(Task task, String type) throws InterruptedException {
        if (task != null) {
            LOGGER.info("Task:{}", task);
            TreeSet<String> set = map.get(type);
            if (set.size() == 0 && type.equals("LITTLE")) {
                set = map.get("MIDDLE");
            } else if (set.size() == 0 && type.equals("MIDDLE")) {
                set = map.get("LARGE");
            }
            String address = LoadBalance.match("RANDOM", LoadBalance.ROUND)
                    .rpcLoadBalance.route("dispatcher", set);
            RpcUtils.exec(address, task.getScript());
        } else {
            LOGGER.info("队列为空，等待任务到来");
            Thread.sleep(2000);
        }
    }

    private void deal(LinkedBlockingQueue<Task> queues, Task task) {
        Task last = queues.peek();
        if (last != null) {
            if (last.getPriority() < task.getPriority()) {
                LOGGER.info("===【FIFO】【添加任务】根据任务优先级调整顺序===");
                synchronized (this) {
                    queues.poll();
                    queues.add(task);
                    queues.add(last);
                }
            } else {
                LOGGER.info("===【FIFO】【添加任务】根据到来先后添加任务===");
                queues.add(task);
            }
        } else {
            LOGGER.info("===【FIFO】【添加任务】前方没有任务，直接添加===");
            queues.add(task);
        }
    }
}
