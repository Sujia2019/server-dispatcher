package com.tute.sujia.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    public static ThreadPoolExecutor ThreadPool(final String name) {
        return new ThreadPoolExecutor(
                60, 300, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), r -> new Thread(r, "rpc" + name + "-Pool-" + r.hashCode()), (r, executor) -> {
                    throw new RuntimeException("rpc"+name+"Thread pool is EXHAUSTED!");
                });
    }
}
