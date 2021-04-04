package com.tute.sujia.dispatcher;

import com.tute.sujia.entity.Task;

import java.util.TreeSet;

public abstract class TaskScheduler {
    public abstract void setAddress(TreeSet<String> addressSet);

    public abstract String dispatch(Task task);

    public abstract void exec();
}
