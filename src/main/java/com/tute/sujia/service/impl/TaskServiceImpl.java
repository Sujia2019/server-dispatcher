package com.tute.sujia.service.impl;

import com.google.gson.Gson;
import com.tute.sujia.dao.DispatcherMapper;
import com.tute.sujia.dao.ServerMapper;
import com.tute.sujia.dao.TaskMapper;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.entity.Task;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.service.TaskService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import com.tute.sujia.utils.RpcUtils;
import com.tute.sujia.utils.ThreadPoolUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    DispatcherMapper dispatcherMapper;
    @Autowired
    ServerMapper serverMapper;
    @Autowired
    ServerService serverService;
    @Autowired
    DispatcherService dispatcherService;
    @Override
    public ReturnT<?> create(Task task) {
        if (null !=taskMapper.getTaskByName(task.getTask_name())){
            LOGGER.error("此任务名称已存在!  {}",task.getTask_name());
            return new ReturnT<>(Constants.FAIL,"此任务名称已存在!  "+task.getTask_name());
        }
        return new ReturnT<>(Constants.SUCCESS,taskMapper.insert(task));
    }

    @Override
    public ReturnT<?> exec(String address,String name) {
        LOGGER.info("将要执行任务：{}",name);
        // TODO 核心：执行任务
        // 获取任务
        Task task = taskMapper.getTaskByName(name);
        if (task == null) {
            LOGGER.error("任务【{}】不存在，请检查参数", name);
            return new ReturnT<>(Constants.FAIL, "任务不存在，请检查参数");
        }
//        if (Strings.isBlank(task.getCron())){
//            // 如果没有设置定时任务，则立即执行
//        }
        try {
            LOGGER.info("======脚本内容【{}】======",task.getScript());
            RpcUtils.exec(address,task.getScript());
            return new ReturnT<>(Constants.SUCCESS,"脚本执行成功");
        } catch (Exception e) {
            LOGGER.error("执行脚本时出错,请检查脚本内容");
            e.printStackTrace();
            return new ReturnT<>(Constants.FAIL,"执行脚本时出错,请检查脚本内容");
        }
    }

    @Override
    public ReturnT<?> delete(String name) {
        return new ReturnT<>(Constants.SUCCESS,taskMapper.delete(name));
    }

    @Override
    public ReturnT<?> modify(Task task) {
        // TODO 校验cron规则
        return new ReturnT<>(Constants.SUCCESS,taskMapper.modify(task));
    }

    @Override
    public ReturnT<?> getTasks() {
        return new ReturnT<>(Constants.SUCCESS,taskMapper.getAll());
    }

    @Override
    public ReturnT<?> insertQueue(List<String> taskNames) {
        for (String name : taskNames) {
            Task task = taskMapper.getTaskByName(name);
            if (task != null) {
                dispatcherService.getDispatcher().dispatcher.dispatch(task);
            } else {
                LOGGER.error("无效的任务：{}", name);
            }
        }
        return new ReturnT<>(Constants.SUCCESS, "添加成功");
    }

}
