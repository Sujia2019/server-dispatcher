package com.tute.sujia.controller;

import com.google.gson.Gson;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.entity.Server;
import com.tute.sujia.entity.Task;
import com.tute.sujia.entity.TaskDTO;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.service.TaskService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TreeSet;

@RequestMapping("/task")
@RestController
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    DispatcherService dispatcherService;
    @Autowired
    ServerService serverService;

    @RequestMapping(value = "create",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> create(@RequestBody Task task){
        return taskService.create(task);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> delete(@RequestBody String name){
        return taskService.delete(name);
    }

    @RequestMapping(value = "modify",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> modify(@RequestBody Task task){
        return taskService.modify(task);
    }

    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> getAll(){
        return taskService.getTasks();
    }


    /**
     * 执行任务---单个执行
     *
     * @param taskName,serverName
     * @return
     */
    @RequestMapping(value = "exec", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> exec(@RequestBody String taskName, String serverName) {
        if (Strings.isBlank(serverName)) {
            return new ReturnT<>(Constants.FAIL, "请选择服务器");
        }
        Server server = serverService.getServer(serverName);
        if (server != null) {
            return taskService.exec(server.getServer_add(), taskName);
        }
        return new ReturnT<>(Constants.FAIL, "未找到该服务器");
    }

    /**
     * 执行任务---经过负载均衡策略
     *
     * @param taskDTO
     * @return
     */
    @RequestMapping(value = "execLoadBalance", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> execLoadBalance(@RequestBody TaskDTO taskDTO) {
        String loadName = taskDTO.getLoadBalance();
        if (Strings.isBlank(loadName)) {
            return new ReturnT<>(Constants.FAIL, "请选择负载均衡策略再执行");
        }
        Dispatcher loadBalance = dispatcherService.getLoadBalance(taskDTO.getLoadBalance());
        TreeSet<String> addressSet;
        String address;
        if (loadBalance != null) {
            addressSet = serverService.getServers(loadBalance.getAddress());
            address = LoadBalance.match(loadName, LoadBalance.ROUND).rpcLoadBalance.route(taskDTO.getTaskName(), addressSet);
            return taskService.exec(address, taskDTO.getTaskName());
        }
        return new ReturnT<>(Constants.FAIL, "未找到负载均衡规则");
    }

    /**
     * 向资源调度队列添加任务
     *
     * @param taskNames
     * @return
     */
    @RequestMapping(value = "addTask", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> addTask(@RequestBody List<String> taskNames) {
        if (dispatcherService.getDispatcher() == null) {
            return new ReturnT<>(Constants.FAIL, "请先设置指定的调度算法");
        } else {
            return taskService.insertQueue(taskNames);
        }
    }

    /**
     * 执行任务---经过资源调度
     *
     * @param
     * @return
     */
    @RequestMapping(value = "execScheduler", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> execScheduler() {
        if (dispatcherService.getDispatcher() == null) {
            return new ReturnT<>(Constants.FAIL, "请先设置策略");
        }
        return dispatcherService.runByDispatcher();
    }

}
