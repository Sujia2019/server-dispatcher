package com.tute.sujia.service;

import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ServiceService {
    /**
     * 获取已注册有效的服务
     * @return
     */
    public ReturnT<?> getServices();

    /**
     * 批量配置服务策略
     * @param services
     * @return
     */
    public ReturnT<?> configure(List<com.tute.sujia.entity.Service> services);

    /**
     * 配置单个服务的负载策略
     * @param services
     * @return
     */
    public ReturnT<?> configure(com.tute.sujia.entity.Service services);

    /**
     * 测试rpc策略
     * @param ids
     * @return
     */
    public ReturnT<?> testRpc(Integer ids,Object[] params);
}
