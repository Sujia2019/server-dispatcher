package com.tute.sujia.service.impl;

import com.google.gson.Gson;
import com.tute.sujia.dao.DispatcherMapper;
import com.tute.sujia.dao.ServiceMapper;
import com.tute.sujia.entity.Dispatcher;
import com.tute.sujia.entity.Service;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.service.ServiceService;
import com.tute.sujia.utils.ClassUtil;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import com.tute.sujia.utils.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tute.sujia.Demo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceMapper serviceMapper;
    @Autowired
    DispatcherMapper dispatcherMapper;
    @Autowired
    ServerService serverService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceServiceImpl.class);

    @Override
    public ReturnT<?> getServices() {
        // 获取所有的方法
        Method[] methods = Demo.class.getMethods();
        List<Service> services = new ArrayList<>();
        for (Method method : methods){
            String methodName = method.getName();
            String route = Demo.class.getName();
            Class<?>[] paramTypes = method.getParameterTypes();
            StringBuilder params = new StringBuilder();
            for (Class<?> type:paramTypes){
                params.append(type.getSimpleName()).append("|");
            }
            // 获取唯一的业务方法实例
            Service service = serviceMapper.getByRoute(methodName,route,params.toString());
            if (service == null){
                service = new Service();
                service.setService_name(methodName);
                service.setMethod_name(methodName);
                service.setService_route(route);
                service.setParam_type(params.toString());
                serviceMapper.insert(service);
            }
            services.add(service);
        }
        return new ReturnT<>(Constants.SUCCESS,services);
    }

    @Override
    public ReturnT<?> configure(List<Service> services) {
        for (Service service:services){
            serviceMapper.modify(service);
        }
        return new ReturnT<>(Constants.SUCCESS,"批量操作成功");
    }

    @Override
    public ReturnT<?> configure(Service services) {
        return new ReturnT<>(Constants.SUCCESS,serviceMapper.modify(services));
    }

    @Override
    public ReturnT<?> testRpc(Integer id,Object[] params) {
        Service services = serviceMapper.getById(id);
        Dispatcher dispatcher = dispatcherMapper.getDispatcherByName(services.getLoad_balance());
        if (dispatcher == null){
            LOGGER.error("负载均衡方法不存在，请检查参数或刷新页面");
            return new ReturnT<>(Constants.FAIL,"负载均衡方法不存在，请检查参数或刷新页面");
        }
        String address ;
        Object response = "";
        if (dispatcher.isAvailable()){
            // 获取地址
            TreeSet<String> add = serverService.getServers(dispatcher.getAddress());
            // 负载均衡计算
            address =LoadBalance.match(services.getLoad_balance(),LoadBalance.ROUND)
                    .rpcLoadBalance.route(services.getService_route(),add);
            // RPC调用
//            response = RpcUtils.testRpc(address,services.getMethod_name(),
//                    ClassUtil.getTypeClass(services.getParam_type()),params);

            if (services.getMethod_name().equals("sayHello")) {
                Demo demo = (Demo) RpcUtils.getImpl(address);
                response = demo.sayHello(params[0].toString());
            } else if (services.getMethod_name().equals("exec")) {
                RpcUtils.exec(address, params[0].toString());
                response = "正在地址为" + address + "的地址上执行脚本" + params[0].toString();
            }
        }else{
            return new ReturnT<>(Constants.FAIL,"负载均衡方法暂不可用");
        }

        return new ReturnT<>(Constants.SUCCESS, response);
    }
}
