package com.tute.sujia.dao;

import com.tute.sujia.entity.Dispatcher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatcherMapper {

    @Insert("insert into dispatcher(type,custom_name,router_ZN,router_EN,address,available,properties) " +
            "values(#{type},#{custom_name},#{router_ZN},#{router_EN},#{address},#{available},#{properties})")
    int insert(Dispatcher dispatcher);

    @Delete("delete from dispatcher where custom_name=#{name}")
    int delete(String name);

    @Update("<script> update dispatcher set custom_name=#{custom_name} and available=#{available}" +
            "   <if test=\"null != type\">" +
            "       type = #{type}," +
            "   </if> and" +
            "   <if test=\"null != router_ZN\">" +
            "       router_ZN = #{router_ZN}," +
            "   </if> and" +
            "   <if test=\"null != router_EN\">" +
            "       router_EN = #{router_EN}," +
            "   </if> and" +
            "   <if test=\"null != address\">" +
            "       address = #{address}," +
            "   </if> and " +
            "   <if test=\"null != properties\">" +
            "       properties = #{properties}," +
            "   </if>" +
            "       where id=#{id}" +
            "   </script>")
    int modify(Dispatcher dispatcher);

    @Select("select * from dispatcher")
    List<Dispatcher> getAll();
    @Select("select * from dispatcher where custom_name like concat('%',#{custom_name},'%')")
    List<Dispatcher> getByName(String custom_name);

    @Select("select * from dispatcher where id=#{id}")
    Dispatcher getById(int id);

    @Select("select * from dispatcher where custom_name=#{name}")
    Dispatcher getDispatcherByName(String name);
}
