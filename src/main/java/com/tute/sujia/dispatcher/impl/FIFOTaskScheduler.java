package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Task;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.utils.RpcUtils;
import com.tute.sujia.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class FIFOTaskScheduler extends TaskScheduler {
    private static LinkedBlockingQueue<Task> queues = new LinkedBlockingQueue<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FIFOTaskScheduler.class);
    private static TreeSet<String> addressSet;

    @Override
    public String dispatch(Task task) {
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
        return "FIFO";
    }

    @Override
    public void setAddress(TreeSet<String> addressSet) {
        FIFOTaskScheduler.addressSet = addressSet;
    }

    @Override
    public void exec() {
        while (true) {
            try {
                Task task = queues.poll();
                if (task != null) {
                    LOGGER.info("Task:{}", task);
                    String address = LoadBalance.match("RANDOM", LoadBalance.ROUND)
                            .rpcLoadBalance.route("dispatcher", addressSet);
                    RpcUtils.exec(address, task.getScript());
                } else {
                    LOGGER.info("队列为空，等待任务到来");
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
