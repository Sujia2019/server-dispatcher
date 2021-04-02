package com.tute.sujia.dispatcher;

import com.tute.sujia.dispatcher.impl.CapacityTaskScheduler;
import com.tute.sujia.dispatcher.impl.FIFOTaskScheduler;
import com.tute.sujia.dispatcher.impl.FairTaskScheduler;

public enum Scheduler {
    // 先进先出顺序调度
    FIFO(new FIFOTaskScheduler()),

    // 公平调度算法
    FAIR(new FairTaskScheduler()),

    // 资源调度
    CAPACITY(new CapacityTaskScheduler());

    public final TaskScheduler dispatcher;

    Scheduler(TaskScheduler dispatcher) {
        this.dispatcher=dispatcher;
    }

    public static Scheduler match(String name, Scheduler defaultScheduler) {
        for (Scheduler item : Scheduler.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultScheduler;
    }
}
