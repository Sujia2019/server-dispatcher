package com.tute.sujia.controller;

import com.tute.sujia.entity.Service;
import com.tute.sujia.service.ServiceService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/service")
@RestController
public class ServiceController {
    @Autowired
    ServiceService serviceService;

    @RequestMapping(value = "configure",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> create(@RequestBody Service service){
        return serviceService.configure(service);
    }

    @RequestMapping(value = "configureMore",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> configure(@RequestBody List<Service> services){
        return serviceService.configure(services);
    }

    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> modify(){
        return serviceService.getServices();
    }
}
