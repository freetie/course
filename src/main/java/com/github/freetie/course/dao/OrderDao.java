package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDao {
    @Insert("INSERT INTO course_order (account_id, course_id) VALUES (#{accountId}, #{courseId})")
    void save(Order order);

    @Select("SELECT * FROM course_order WHERE account_id = #{accountId}")
    List<Order> findAllByAccountId(@Param("accountId") Integer accountId);
}
