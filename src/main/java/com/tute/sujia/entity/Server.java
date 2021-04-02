package com.tute.sujia.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Server implements Serializable {
    private int server_id;
    private String server_key;
    private String server_add;
    private String server_name;
    private String server_user;
    private String server_pwd;
    private String server_detail;
    private boolean server_status;
    private int server_memory;
    private int server_disk;
    private String create_time;
    private String last_modify;
    private String creator;
    private String last_executor;
}
