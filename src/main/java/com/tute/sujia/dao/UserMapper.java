package com.tute.sujia.dao;

import com.tute.sujia.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Insert("insert into user(user_account,user_name,user_pwd,server_per,service_per,task_per,run_per,dispatcher_per)" +
            "values(#{user_account},#{user_name}m#{user_pwd},#{server_per},#{service_per},#{task_per},#{run_per},#{dispatcher_per})")
    void insert(User user);
    @Delete("delete from user where user_account=#{account}")
    void delete(String account);

    @Update("update from user set user_name=#{user_name} and user_pwd=#{user_pwd} and server_per=#{server_per}" +
            "and service_per=#{service_per} and task_per=#{task_per} and run_per=#{run_per} and dispatcher_per=#{dispatcher_per} where user_account=#{user_account}")
    void modify(User user);

    @Select("select * from user")
    List<?> getAll();

    @Select("select * from user where user_name like concat('%',#{name},'%')")
    List<?> getByName(String name);
    @Select("select * from user where user_account=#{account}")
    User getByAccount(String account);
    @Select("select * from user where user_name=#{name}")
    User getUserByName(String name);

}
