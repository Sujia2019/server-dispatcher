package com.tute.sujia.service.impl;

import com.google.gson.Gson;
import com.tute.sujia.dao.ServerMapper;
import com.tute.sujia.entity.Server;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

@Service
public class ServerServiceImpl implements ServerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceImpl.class);
    @Autowired
    ServerMapper serverMapper;

    @Override
    public ReturnT<?> registServer(Server server) {
        if (null !=serverMapper.getByKey(server.getServer_key())){
            LOGGER.error("此服务器序列号已被注册!  {}",server.getServer_key());
            return new ReturnT<>(Constants.FAIL,"此服务器序列号已被注册!  "+server.getServer_key());
        }
        if (null != serverMapper.getServerByName(server.getServer_name())){
            LOGGER.error("已存在的服务器--名为:{}",server.getServer_name());
            return new ReturnT<>(Constants.FAIL,"已存在的服务器--名为:"+server.getServer_name());
        }
        return new ReturnT<>(Constants.SUCCESS,serverMapper.insert(server));
    }

    @Override
    public ReturnT<?> modifyServer(Server server) {
        return new ReturnT<>(Constants.SUCCESS,serverMapper.modify(server));
    }

    @Override
    public ReturnT<?> delServer(String key) {
        return new ReturnT<>(Constants.SUCCESS,delServer(key));
    }

    @Override
    public TreeSet<String> getServers() {
        return getServers(null);
    }

    @Override
    public TreeSet<String> getServers(String json) {
        TreeSet<String> add = new TreeSet<>();
        if (Strings.isBlank(json)){
            // 若没有指定服务器则默认全部
            LOGGER.info("未指定服务器，默认全部");
            List<String> serverList = serverMapper.getAllAddress();
            add.addAll(serverList);
        }else {
            // 解析json数组 {["address":"192.168.80.50:9999"]}
            LOGGER.info("服务器列表参数:{}", json);
            Gson gson = new Gson();
            add = gson.fromJson(json,TreeSet.class);
        }
        return add;
    }

    @Override
    public Server getServer(String name) {
        return serverMapper.getServerByName(name);
    }


}
