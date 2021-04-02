package com.tute.sujia.dispatcher;

import com.tute.sujia.dispatcher.impl.CapacityTaskDispatcher;
import com.tute.sujia.dispatcher.impl.FIFOTaskDispatcher;
import com.tute.sujia.dispatcher.impl.FairTaskDispatcher;

public enum  Dispatcher {
    // 先进先出顺序调度
    FIFO(new FIFOTaskDispatcher()),

    // 公平调度算法
    FAIR(new FairTaskDispatcher()),

    // 资源调度
    CAPACITY(new CapacityTaskDispatcher());

    public final TaskDispatcher dispatcher;

    Dispatcher(TaskDispatcher dispatcher){
        this.dispatcher=dispatcher;
    }

    public static Dispatcher match(String name,Dispatcher defaultDispatcher){
        for (Dispatcher item:Dispatcher.values()){
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultDispatcher;
    }
}
