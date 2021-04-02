package com.tute.sujia.controller;

import com.tute.sujia.entity.Task;
import com.tute.sujia.service.TaskService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/task")
@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

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

//    /**
//     * 执行任务
//     * @param name
//     * @return
//     */
//    @RequestMapping(value = "exec",method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnT<?> run(@RequestBody String name){
//        return taskService.exec(name);
//    }

}
