package com.tute.sujia.dispatcher.impl;

import com.tute.sujia.dispatcher.TaskDispatcher;
import com.tute.sujia.entity.Task;

import java.util.TreeSet;

public class FIFOTaskDispatcher extends TaskDispatcher {
    @Override
    public String dispatch(Task task, TreeSet<String> addressSet) {
        return null;
    }
}
