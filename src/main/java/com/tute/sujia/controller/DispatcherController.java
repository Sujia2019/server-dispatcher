package com.tute.sujia.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.utils.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dispatcher")
@RestController
public class DispatcherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherController.class);
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

    @RequestMapping(value = "setDispatcher", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> setDispatcher(@RequestBody String name) {
        LOGGER.info(name);

        JsonObject returnData = new JsonParser().parse(name).getAsJsonObject();
        String dispatcherName = returnData.get("name").getAsString();
        return dispatcherService.setDispatcher(dispatcherName);
    }

}
