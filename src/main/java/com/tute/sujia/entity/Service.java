package com.tute.sujia.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Service implements Serializable {
    private int id;
    private String service_name;
    private String method_name;
    private String service_route; // 业务方法全路径
    private String param_type; // 参数类型
    private String load_balance; // 负载均衡策略

}
