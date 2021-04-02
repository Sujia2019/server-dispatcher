package com.tute.sujia.dao;

import com.tute.sujia.entity.Service;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceMapper {
    @Insert("insert into service(service_name,method_name,service_route,param_type) " +
            "values(#{service_name},#{method_name},#{service_route},#{param_type})")
    int insert(Service service);
    @Delete("delete from service where service_route=#{service_route}")
    int delete(String service_route);
    @Update("update service set service_name=#{service_name} " +
            "and load_balance=#{load_balance} where service_route=#{service_route}")
    int modify(Service service);

    @Select("select * from service")
    List<Service> getAll();
    @Select({
            "<script>" +
                    "select * from service where id in " +
                    "<foreach item = 'item' index = 'index' collection = 'ids' open='(' separator=',' close=')'>" +
                    "#{item}" +
                    "</foreach>"+
                    "</script>"})
    List<Service> getByIds(@Param("ids") List<Integer> ids);

    @Select("select * from service where service_name like concat('%',#{name},'%')")
    List<Service> getByName(String name);
    @Select("select * from service where method_name like concat('%',#{method_name},'%')")
    List<Service>  getByMethod(String method_name);

    // 通过方法名，参数列表，接口路径获取唯一的方法
    @Select("select * from service where method_name=#{method} and service_route=#{route} and param_type=#{param_type}")
    Service getByRoute(String method,String route,String param_type);
    @Select("select * from service where id=#{id}")
    Service getById(Integer id);
}
