package com.tute.sujia.service;

import com.tute.sujia.entity.Server;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;

import java.util.TreeSet;


public interface ServerService {

    ReturnT<?> registServer(Server server);

    ReturnT<?> modifyServer(Server server);

    ReturnT<?> delServer(String key);

    TreeSet<String> getServers();

    TreeSet<String> getServers(String json);


}
