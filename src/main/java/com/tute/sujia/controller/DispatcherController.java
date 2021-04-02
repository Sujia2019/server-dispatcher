package com.tute.sujia.controller;

import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dispatcher")
@RestController
public class DispatcherController {
    @Autowired
    DispatcherService dispatcherService;

    @RequestMapping(value = "create",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> create(@RequestBody Dispatcher dispatcher){
        return dispatcherService.create(dispatcher);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> delete(@RequestBody String name){
        return dispatcherService.delete(name);
    }

    @RequestMapping(value = "modify",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> modify(@RequestBody Dispatcher dispatcher){
        return dispatcherService.modify(dispatcher);
    }

    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> getAll(){
        return dispatcherService.getDispatchers();
    }

}
