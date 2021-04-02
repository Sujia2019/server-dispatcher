package com.tute.sujia.dao;

import com.tute.sujia.entity.Server;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerMapper {
    @Insert("insert into server(server_key,server_add,server_name,server_user,server_pwd,server_detail,server_status,server_memory,server_disk,create_time,last_modify,creator,last_executor) " +
            "values(#{server_key},#{server_add},#{server_name},#{server_user},#{server_pwd},#{server_detail},#{server_status},#{server_memory},#{server_disk},now(),now(),#{creator},#{last_executor})")
    int insert(Server server);
    @Delete("delete from server where server_key=#{key}")
    int delete(String key);
    @Update("<script> update server set last_executor=#{last_executor} and server_memory=#{server_memory} and server_disk=#{server_disk}" +
            "and server_status=#{server_status} and last_modify=now()" +
            "   <if test=\"null != server_add\">" +
            "       server_add = #{server_add}," +
            "   </if> and" +
            "   <if test=\"null != server_name\">" +
            "       server_name = #{server_name}," +
            "   </if> and" +
            "   <if test=\"null != server_user\">" +
            "       server_user = #{server_user}," +
            "   </if> and" +
            "   <if test=\"null != server_pwd\">" +
            "       server_pwd = #{server_pwd}," +
            "   </if> and" +
            "   <if test=\"null != server_detail\">" +
            "       server_detail = #{server_detail}," +
            "   </if> "+
            "       where server_key=#{server_key}" +
            "   </script>")
    int modify(Server server);

    @Select("select * from server")
    List<Server> getAll();
    @Select("select server_add from server")
    List<String> getAllAddress();

    @Select("select * from server where server_name like concat('%',#{name},'%')")
    List<Server> getServersByName(String name);

    @Select("select * from server where server_key = #{key}")
    Server getByKey(String key);

    @Select("select * from server where server_name=#{name}")
    Server getServerByName(String name);
}
