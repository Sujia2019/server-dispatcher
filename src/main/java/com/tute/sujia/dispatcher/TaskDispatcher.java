package com.tute.sujia.dispatcher;

import com.tute.sujia.entity.Task;

import java.util.TreeSet;

public abstract class TaskDispatcher {
    public abstract String dispatch(Task task, TreeSet<String> addressSet);
}
