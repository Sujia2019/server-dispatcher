package com.tute.sujia.utils;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {

    public static Method[] getMethods(Class<?> clazz){
        return clazz.getMethods();
    }


    /**
     * 将记录的字符串类型转化为类
     * @param paramType
     * @return
     */
    public static Class<?>[] getTypeClass(String paramType){
        String[] sub = paramType.split("\\|");
        int length = sub.length;
        System.out.println(length);
        Class<?>[] classes = new Class<?>[length];
        try {
            for (int i=0;i<length;i++){
                classes[i] = Class.forName("java.lang."+sub[i]);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
