package com.tute.sujia.router;

import java.util.TreeSet;

public abstract class RpcLoadBalance {
    public abstract String route(String serviceKey, TreeSet<String> addressSet);
}
