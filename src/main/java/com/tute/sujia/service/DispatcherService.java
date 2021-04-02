package com.tute.sujia.service;

import com.tute.sujia.dispatcher.Scheduler;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.utils.ReturnT;


public interface DispatcherService {

    public ReturnT<?> create(Dispatcher task);

    public ReturnT<?> delete(String name);

    public ReturnT<?> modify(Dispatcher task);

    public ReturnT<?> getDispatchers();

    public ReturnT<?> setDispatcher(String name);

    public Scheduler getDispatcher();

    public ReturnT<?> runByDispatcher();
}
