package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Task;

import java.util.TreeSet;

public class CapacityTaskScheduler extends TaskScheduler {
    @Override
    public void setAddress(TreeSet<String> addressSet) {

    }

    @Override
    public String dispatch(Task task) {
        return null;
    }

    @Override
    public String exec() {
        return "CAPACITY";
    }
}
