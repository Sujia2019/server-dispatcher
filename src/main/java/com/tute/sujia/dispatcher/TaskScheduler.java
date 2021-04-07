package com.tute.sujia.dispatcher;

import com.tute.sujia.entity.Server;
import com.tute.sujia.entity.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public abstract class TaskScheduler {
    public abstract void setAddress(TreeSet<String> addressSet, Map<String, Integer> addressCapacity);

    public abstract String dispatch(Task task);

    public abstract void exec();

}
