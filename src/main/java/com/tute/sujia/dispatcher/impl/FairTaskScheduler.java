package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dao.ServerMapper;
import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Server;
import com.tute.sujia.entity.Task;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.utils.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class FairTaskScheduler extends TaskScheduler {
    private LinkedBlockingQueue<Task> queues = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(FairTaskScheduler.class);
    private static TreeSet<String> addressSet;
    private static int totalCapacity = 0;
    private static Map<String, Integer> addressCapacity = new HashMap<>();

//    @Autowired
//    ServerMapper serverMapper;

    @Override
    public void setAddress(TreeSet<String> addressSet, Map<String, Integer> addressCapacity) {
        FairTaskScheduler.addressSet = addressSet;
        FairTaskScheduler.addressCapacity = addressCapacity;
        totalCapacity = 0;
        for (String s : addressSet) {
            totalCapacity += addressCapacity.get(s);
            addressCapacity.put(s, addressCapacity.get(s));
        }

    }

    @Override
    public String dispatch(Task task) {
        if (queues == null) {
            queues = new LinkedBlockingQueue<>();
        }
        queues.add(task);
        return "FAIR";
    }
    @Override
    public void exec() {
        while (true) {
            Task top = queues.peek();
            if (top != null) {
                if (top.getCapacity() < totalCapacity) {
                    queues.poll();
                    String address = LoadBalance.match("LRU", LoadBalance.ROUND)
                            .rpcLoadBalance.route("dispatcher", addressSet);
                    address = calculateAddress(top, address);

                    RpcUtils.exec(address, top.getScript());

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    totalCapacity += top.getCapacity();
                    int after = addressCapacity.get(address);
                    after += top.getCapacity();
                    addressCapacity.put(address, after);
                }
            } else {
                LOGGER.info("执行完毕");
                break;
            }
        }

    }

    private String calculateAddress(Task task, String address) {
        int before = addressCapacity.get(address);
        if (task.getCapacity() < before) {
            addressCapacity.put(address, before - task.getCapacity());
            totalCapacity -= task.getCapacity();
            return address;
        } else if (task.getCapacity() == before) {
            addressCapacity.put(address, 0);
            totalCapacity -= task.getCapacity();
            addressSet.remove(address);
            return address;
        } else {
            // 从其他有资源的机器上计算
            TreeSet<String> local = new TreeSet<>(addressSet);
            local.remove(address);
            address = LoadBalance.match("RANDOM", LoadBalance.ROUND)
                    .rpcLoadBalance.route("dispatcher", local);
            return calculateAddress(task, address);
        }
    }
}
