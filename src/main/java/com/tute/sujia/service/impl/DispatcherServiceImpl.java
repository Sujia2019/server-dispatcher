package com.tute.sujia.service.impl;

import com.tute.sujia.dispatcher.Scheduler;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispatcherServiceImpl implements DispatcherService {
    private static Scheduler scheduler = null;
    @Autowired
    ServerService serverService;
    @Override
    public ReturnT<?> create(Dispatcher task) {
        return null;
    }

    @Override
    public ReturnT<?> delete(String name) {
        return null;
    }

    @Override
    public ReturnT<?> modify(Dispatcher task) {
        return null;
    }

    @Override
    public ReturnT<?> getDispatchers() {
        return null;
    }

    @Override
    public ReturnT<?> setDispatcher(String name) {
        scheduler = Scheduler
                .match(name, Scheduler.FIFO);
        return new ReturnT<>(Constants.SUCCESS, "设置成功");
    }

    @Override
    public Scheduler getDispatcher() {
        return scheduler;
    }

    @Override
    public ReturnT<?> runByDispatcher() {
        String scheduler = DispatcherServiceImpl.scheduler.dispatcher.exec();
        return new ReturnT<>(Constants.SUCCESS, "正在执行调度器" + scheduler);
    }
}
