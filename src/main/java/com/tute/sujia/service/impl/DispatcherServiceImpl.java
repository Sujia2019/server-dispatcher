package com.tute.sujia.service.impl;

import com.tute.sujia.dao.DispatcherMapper;
import com.tute.sujia.dao.ServerMapper;
import com.tute.sujia.dispatcher.Scheduler;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.entity.Server;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@Service
public class DispatcherServiceImpl implements DispatcherService {
    private static Scheduler scheduler = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServiceImpl.class);
    @Autowired
    ServerService serverService;
    @Autowired
    DispatcherMapper dispatcherMapper;
    @Autowired
    ServerMapper serverMapper;

    @Override
    public ReturnT<?> create(Dispatcher dispatcher) {
        if (dispatcherMapper.getDispatcherByName(dispatcher.getCustom_name()) != null) {
            return new ReturnT<>(Constants.FAIL, "已有该策略");
        }
        return new ReturnT<>(Constants.SUCCESS, dispatcherMapper.insert(dispatcher));
    }

    @Override
    public ReturnT<?> delete(String name) {
        return new ReturnT<>(Constants.SUCCESS, dispatcherMapper.delete(name));
    }

    @Override
    public ReturnT<?> modify(Dispatcher dispatcher) {
        return new ReturnT<>(Constants.SUCCESS, dispatcherMapper.modify(dispatcher));
    }

    @Override
    public ReturnT<?> getDispatchers() {
        return new ReturnT<>(Constants.SUCCESS, dispatcherMapper.getAll());
    }

    @Override
    public ReturnT<?> setDispatcher(String name) {
        Dispatcher dispatcher = dispatcherMapper.getDispatcherByName(name);
        if (dispatcher != null) {
            name = dispatcher.getRouter_EN();
            scheduler = Scheduler
                    .match(name, Scheduler.FIFO);
            TreeSet<String> addressSet = serverService.getServers(dispatcher.getAddress());
            setAddress(addressSet);
            return new ReturnT<>(Constants.SUCCESS, "设置成功");
        }
        return new ReturnT<>(Constants.FAIL, "设置失败，未找到对应的策略");
    }

    @Override
    public Scheduler getDispatcher() {
        return scheduler;
    }

    @Override
    public ReturnT<?> runByDispatcher() {
        DispatcherServiceImpl.scheduler.dispatcher.exec();
        return new ReturnT<>(Constants.SUCCESS, "正在执行调度器 " + scheduler.name());
    }

    @Override
    public void setAddress(TreeSet<String> address) {
        if (scheduler == null) {
            scheduler = Scheduler
                    .match("FIFO", Scheduler.FIFO);
        }
        Map<String, Integer> addressCapacity = new HashMap<>();
        for (String s : address) {
            Server server = serverMapper.getServerByAddress(s);
            addressCapacity.put(s, server.getServer_memory());
        }
        scheduler.dispatcher.setAddress(address, addressCapacity);
    }

    @Override
    public Dispatcher getLoadBalance(String name) {
        return dispatcherMapper.getDispatcherByName(name);
    }
}
