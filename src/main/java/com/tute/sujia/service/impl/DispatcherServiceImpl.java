package com.tute.sujia.service.impl;

import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;

@Service
public class DispatcherServiceImpl implements DispatcherService {
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
}
