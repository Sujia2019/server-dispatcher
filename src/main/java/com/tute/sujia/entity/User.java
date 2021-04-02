package com.tute.sujia.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private int user_id;
    private String user_account;
    private String user_name;
    private String user_pwd;
    private boolean server_per;
    private boolean service_per;
    private boolean task_per;
    private boolean run_per;
    private boolean dispatcher_per;
}
