package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dispatcher.TaskScheduler;
import com.tute.sujia.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class FairTaskScheduler extends TaskScheduler {
    private LinkedBlockingQueue<Task> queues = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(FairTaskScheduler.class);
    private static TreeSet<String> addressSet;

    @Override
    public void setAddress(TreeSet<String> addressSet) {
        FairTaskScheduler.addressSet = addressSet;
    }

    @Override
    public String dispatch(Task task) {
        if (queues == null) {
            queues = new LinkedBlockingQueue<>();
        }
        queues.add(task);
        return "FAIR";
    }
    @Override
    public void exec() {

    }
}
