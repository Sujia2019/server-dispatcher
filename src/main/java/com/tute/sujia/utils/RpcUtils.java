package com.tute.sujia.utils;


import com.tute.sujia.Demo;
import com.tute.sujia.net.RpcReferenceBean;
import com.tute.sujia.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcUtils.class);

    private static RpcReferenceBean referenceBean = new RpcReferenceBean();
    static {
        referenceBean.setIface(Demo.class);
        referenceBean.setSerializer(Serializer.SerializerEnum.JACKSON.getSerializer());
    }
    public static void exec(final String address,String script){
        Demo demo = (Demo) getImpl(address);
        LOGGER.info("======开始远程执行脚本任务======");
        // 远程执行任务
        demo.exec(script);

//        System.out.println("=========");
//        Object say = demo.sayHello("sujia");
//        LOGGER.info("======执行sayHello方法:"+say);
    }

    public static void addService(){

    }

    public static void setIfase(Class<?> clazz){
        referenceBean.setIface(clazz);
    }

    private static Object getImpl(final String address){
        referenceBean.setAddress(address);
        referenceBean.setVersion(null);
        referenceBean.init();
        return referenceBean.getObject();
    }

    public static Object testRpc(final String address,String methodName,Class<?>[] args,Object[] objs) {
        Demo demo = (Demo) getImpl(address);

        /*
        demo.methodName(objs)
         */

        Class cDemo = demo.getClass();
        try {
            Method method = cDemo.getMethod(methodName,args);
            return method.invoke(method,objs);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return e.getMessage();
        }



    }
}
