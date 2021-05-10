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

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CapacityTaskScheduler extends TaskScheduler {
    private static LinkedBlockingQueue<Task> little = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Task> middle = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<Task> large = new LinkedBlockingQueue<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FIFOTaskScheduler.class);
    private static Map<String, Integer> capacity = new HashMap<>();
    private static Map<String, Integer> addressCapacity = new HashMap<>();
    private static List<String> littleAddress = new ArrayList<>();
    private static List<String> middleAddress = new ArrayList<>();
    private static List<String> largeAddress = new ArrayList<>();
    //    @Autowired
//    ServerMapper serverMapper;

    @Override
    public void setAddress(TreeSet<String> addressSet, Map<String, Integer> addressCapacity) {
        CapacityTaskScheduler.addressCapacity = addressCapacity;
        int totalCapacity = 0;
        // 计算比例
        for (String s : addressSet) {
            totalCapacity += addressCapacity.get(s);
        }
        LOGGER.info("totalCapacity:{}", totalCapacity);
        capacity.put("LITTLE", (totalCapacity / 10) * 2);
        capacity.put("MIDDLE", (totalCapacity / 10) * 3);
        capacity.put("LARGE", (totalCapacity / 10) * 5);

        int cLittle = capacity.get("LITTLE");
        int cMiddle = capacity.get("MIDDLE");
        int cLarge = capacity.get("LARGE");

        LOGGER.info("little:{}  middle:{}  large:{}", cLittle, cMiddle, cLarge);

        HashMap<String, Integer> availableChoose = new HashMap<>(addressCapacity);
        List<String> addressList = new ArrayList<>(addressSet);

        for (int i = 0; i < addressList.size(); ) {
            String s = addressList.get(i);
            int serverMemory = availableChoose.get(s);
            if (serverMemory <= cLittle) {
                littleAddress.add(s);
                cLittle -= serverMemory;
                i++;
                continue;
            } else if (cLittle != 0) {
                littleAddress.add(s);
                availableChoose.put(s, serverMemory - cLittle);
                cLittle = 0;
                continue;
            } else {
                i++;
            }
            if (serverMemory <= cMiddle) {
                middleAddress.add(s);
                cMiddle -= serverMemory;
                i++;
                continue;
            } else if (cMiddle != 0) {
                middleAddress.add(s);
                availableChoose.put(s, serverMemory - cMiddle);
                cMiddle = 0;
                continue;
            } else {
                i++;
            }
            if (serverMemory <= cLarge) {
                largeAddress.add(s);
                cLarge -= serverMemory;
                i++;
            } else if (cLarge != 0) {
                largeAddress.add(s);
                availableChoose.put(s, serverMemory - cLarge);
                cLarge = 0;
            } else {
                i++;
            }

        }
    }

    @Override
    public String dispatch(Task task) {
        LOGGER.info("capacity dispatch: {}", task.toString());
        LOGGER.info("capacity size: {}", capacity.size());
        if (task.getCapacity() <= capacity.get("LITTLE")) {
            deal(little, task);
        } else if (task.getCapacity() <= capacity.get("MIDDLE")) {
            deal(middle, task);
        } else {
            deal(large, task);
        }
        return "CAPACITY";
    }

    @Override
    public void exec() {
        while (true) {
            try {
                if (isSatisfy(little, capacity.get("LITTLE"))) {
                    Task tLittle = little.poll();
                    exec(tLittle, littleAddress, "LITTLE");
                    LOGGER.info("在小队列中执行任务：{}", tLittle);
                }
                if (isSatisfy(middle, capacity.get("MIDDLE"))) {
                    Task tMiddle = middle.poll();
                    exec(tMiddle, middleAddress, "MIDDLE");
                    LOGGER.info("在中队列中执行任务：{}", tMiddle);
                }
                if (isSatisfy(large, capacity.get("LARGE"))) {
                    Task tLarge = large.poll();
                    exec(tLarge, largeAddress, "LARGE");
                    LOGGER.info("在大队列中执行任务：{}", tLarge);
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSatisfy(LinkedBlockingQueue<Task> queue, Integer cap) {
        Task task = queue.peek();
        if (task != null) {
            LOGGER.info("task:{}", task.getCapacity());
            LOGGER.info("capacity:{}", cap);
            // 如果队列获取的容量 大于 所需容量
            if (cap > task.getCapacity()) {
                return true;
            } else {
                LOGGER.info("不满足资源条件");
            }
        }
        return false;
    }


    private void exec(Task task, List<String> addressList, String queueKey) throws InterruptedException {
        if (task != null) {
            LOGGER.info("Task:{}", task);
            TreeSet<String> set = new TreeSet<>(addressList);
            String address = LoadBalance.match("RANDOM", LoadBalance.ROUND)
                    .rpcLoadBalance.route("dispatcher", set);
            RpcUtils.exec(address, task.getScript());

            int before = capacity.get(queueKey);
            capacity.put(queueKey, before - task.getCapacity());
            Thread.sleep(2000);

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
