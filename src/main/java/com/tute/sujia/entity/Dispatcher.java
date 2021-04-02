package com.tute.sujia.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dispatcher implements Serializable {
    private int id;
    private String type; // 类型是负载均衡还是调度算法
    private String custom_name; // 自定义名称
    private String router_ZN;
    private String router_EN;
    private String address; // 指定的地址，若空则默认全部
    private boolean available;
    private String properties;

}
