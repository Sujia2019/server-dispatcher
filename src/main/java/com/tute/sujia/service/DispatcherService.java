package com.tute.sujia.service;

import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;


public interface DispatcherService {

    public ReturnT<?> create(Dispatcher task);

    public ReturnT<?> delete(String name);

    public ReturnT<?> modify(Dispatcher task);

    public ReturnT<?> getDispatchers();
}
