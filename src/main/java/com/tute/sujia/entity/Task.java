package com.tute.sujia.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private int id;
    private String task_name;
    private String script; // 脚本内容
    private String cron;  // 定时cron
    private String detail;
    private int capacity;
    private int priority;
    private String creator;
    private String create_time;
    private String last_modify;
}
