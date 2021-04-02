package com.tute.sujia.dao;

import com.tute.sujia.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {
    @Insert("insert into task(task_name,script,cron,detail,capacity,priority,creator,create_time,last_modify) " +
            "values(#{task_name},#{script},#{cron},#{detail},#{capacity},#{priority},#{creator},now(),now())")
    int insert(Task task);

    @Delete("delete from task where task_name=#{task_name}")
    int delete(String task_name);

    @Update("<script>update from task set script=#{script} and last_modify=now() and priority=#{priority} and capacity=#{capacity}"+
            " <if test=\"null != task_name\">" +
            "       task_name = #{task_name}," +
            "   </if> and" +
            "   <if test=\"null != cron\">" +
            "       cron = #{cron}," +
            "   </if> and" +
            "   <if test=\"null != detail\">" +
            "       detail = #{detail}," +
            "   </if>"+
            "       where id=#{id}" +
            "   </script>")
    int modify(Task task);

    @Select("select * from task")
    List<?> getAll();
    @Select("select * from task where task_name like concat('%',#{name},'%')")
    List<?> getByName(String name);
    @Select("select * from task where id=#{id}")
    Task getById(int id);

    @Select("select * from task where task_name=#{name}")
    Task getTaskByName(String name);
}
