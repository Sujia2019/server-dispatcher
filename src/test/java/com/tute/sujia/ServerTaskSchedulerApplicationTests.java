package com.tute.sujia;

import com.tute.sujia.dao.TaskMapper;
import com.tute.sujia.entity.Server;
import com.tute.sujia.entity.Task;
import com.tute.sujia.net.RpcReferenceBean;
import com.tute.sujia.router.LoadBalance;
import com.tute.sujia.service.DispatcherService;
import com.tute.sujia.service.ServerService;
import com.tute.sujia.service.ServiceService;
import com.tute.sujia.service.TaskService;
import com.tute.sujia.utils.RpcUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@SpringBootTest
class ServerTaskSchedulerApplicationTests {
    @Autowired
    TaskService taskService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    ServerService serverService;
    @Autowired
    DispatcherService dispatcherService;
    @Autowired
    TaskMapper taskMapper;

    @Test
    void contextLoads() {
    }

    /**
     * 服务器注册
     */
    @Test
    void registServer(){
        Server server1 = new Server();
        server1.setServer_key("xxx-xxx-xxx-sujia1");
        server1.setServer_add("192.168.56.111:8888");
        server1.setServer_name("sujia1");
        server1.setServer_user("sujia");
        server1.setServer_pwd("19990315");
        server1.setServer_detail("虚拟机1号");
        server1.setServer_status(true);
        server1.setServer_memory(3096);
        server1.setCreator("admin");

        Server server2 = new Server();
        server2.setServer_key("xxx-xxx-xxx-sujia2");
        server2.setServer_add("192.168.56.114:8888");
        server2.setServer_name("sujia2");
        server2.setServer_user("sujia");
        server2.setServer_pwd("19990315");
        server2.setServer_detail("虚拟机2号");
        server2.setServer_status(true);
        server2.setServer_memory(3096);
        server2.setCreator("admin");

        Server server3 = new Server();
        server3.setServer_key("xxx-xxx-xxx-sujia3");
        server3.setServer_add("192.168.56.115:8888");
        server3.setServer_name("sujia3");
        server3.setServer_user("sujia");
        server3.setServer_pwd("19990315");
        server3.setServer_detail("虚拟机3号");
        server3.setServer_status(true);
        server3.setServer_memory(3096);
        server3.setCreator("admin");
        serverService.registServer(server1);
        serverService.registServer(server2);
        serverService.registServer(server3);
    }

    /**
     * 测试单个任务执行
     */
    @Test
    void testTask(){
        String serviceKey = "service";
        // 获取服务器地址
        TreeSet<String> addressSet = serverService.getServers();
//        TreeSet<String> addressSet = new TreeSet<String>(){{
//            add("192.168.56.111:8888");
//            add("192.168.56.115:8888");
//            add("192.168.56.114:8888");
//            add("192.168.56.112:8888");
//            add("192.168.56.113:8888");
//        }};

        // 根据负载均衡策略执行任务
        String address = LoadBalance.match("CONSISTENT_HASH",LoadBalance.ROUND)
                .rpcLoadBalance.route(serviceKey,addressSet);
        taskService.exec(address,"test");
    }

    /**
     * 获取接口列表
     */
    @Test
    void testGetServices(){
        System.out.println(serviceService.getServices());
    }

    /**
     * RPC测试
     */
    @Test
    void testRpc(){
        // 模板
        RpcReferenceBean referenceBean = new RpcReferenceBean();
        // 注册接口
        referenceBean.setIface(Demo.class);
        // 指定服务器
        referenceBean.setAddress("127.0.0.1:8888");
        // 创建客户端连接
        referenceBean.init();
        // 获取接口实现类
        Demo demo = (Demo) referenceBean.getObject();
        System.out.println("==============");
        // 执行方法
        Object say = demo.sayHello("sujia");
        System.out.println("======执行sayHello方法,返回结果 :" + say);

    }

    /**
     * 负载均衡测试
     */
    @Test
    void loadBalanceTest(){
        String serviceKey = "service";
        TreeSet<String> addressSet = new TreeSet<String>(){{
            add("192.168.56.111:8888");
            add("192.168.56.115:8888");
            add("192.168.56.114:8888");
            add("192.168.56.112:8888");
            add("192.168.56.113:8888");
        }};

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            //  -------------------------------------算法名称，如果未找到则执行默认策略
            String address = LoadBalance.match("ROUND",LoadBalance.ROUND).rpcLoadBalance.route(serviceKey,addressSet);
            System.out.println(address);
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start));

    }

    /**
     * 创建任务
     */
    @Test
    void createTask(){
        Task task = new Task();
        task.setTask_name("xxx1");
        task.setScript("mkdir /home/sujia/project/xxx1");
        task.setDetail("测试创建任务1");
        task.setCreator("admin");
        task.setPriority(0);
        task.setCapacity(0);

        Task task2 = new Task();
        task2.setTask_name("xxx2");
        task2.setScript("mkdir /home/sujia/project/xxx2");
        task2.setDetail("测试创建任务2");
        task2.setCreator("admin");
        task2.setPriority(1);
        task2.setCapacity(1);

        taskService.create(task);
        taskService.create(task2);

    }

    /**
     * 修改任务
     */
    @Test
    void modifyTask(){

    }

    /**
     * 修改接口负载均衡策略
     */
    @Test
    void modifyService(){

    }

    /**
     * 测试任务调度算法
     */
    @Test
    void scheduler(){
        TreeSet<String> addressSet = new TreeSet<String>() {{
            add("192.168.56.111:8888");
            add("192.168.56.115:8888");
            add("192.168.56.114:8888");
        }};
        // 设置策略
        dispatcherService.setDispatcher("FIFO");
        // 设置服务器地址
        dispatcherService.setAddress(addressSet);
        // 添加任务
        List<String> taskNames = new ArrayList<>();
        taskNames.add("xxx1");
        taskNames.add("xxx2");
        taskService.insertQueue(taskNames);
        // 按策略执行
        dispatcherService.runByDispatcher();

    }


}
