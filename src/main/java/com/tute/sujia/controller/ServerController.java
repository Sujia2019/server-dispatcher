package com.tute.sujia.controller;

import com.tute.sujia.entity.Server;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/server")
@RestController
public class ServerController {
    @Autowired
    ServerService serverService;

    @RequestMapping(value = "create",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> create(@RequestBody Server server){
        return serverService.registServer(server);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> delete(@RequestBody String key){
        return serverService.delServer(key);
    }

    @RequestMapping(value = "modify",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> modify(@RequestBody Server server){
        return serverService.modifyServer(server);
    }

    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> getAll(){
        return new ReturnT<>(Constants.SUCCESS, serverService.getAll());
    }

    @RequestMapping(value = "getAvailableServers", method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> getAvailableServers() {
        return new ReturnT<>(Constants.SUCCESS, serverService.getAvailableServers());
    }
}
