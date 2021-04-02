package com.tute.sujia.service;

import com.tute.sujia.entity.Task;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;


public interface TaskService {

    public ReturnT<?> create(Task task);

    /**
     * 执行任务
     * @param address 地址
     * @param name 任务名称
     * @return
     */
    public ReturnT<?> exec(String address,String name);

    public ReturnT<?> delete(String name);

    public ReturnT<?> modify(Task task);

    public ReturnT<?> getTasks();

    /**
     * 加入任务队列
     * @return
     */
    public ReturnT<?> insertQueue();
}
