package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Task;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

@Component
public class FairTaskScheduler extends TaskScheduler {
    @Override
    public void setAddress(TreeSet<String> addressSet) {

    }

    @Override
    public String dispatch(Task task) {
        return null;
    }

    @Override
    public void exec() {


    }
}
