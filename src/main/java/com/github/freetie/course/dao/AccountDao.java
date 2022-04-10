package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountDao {
    @Insert("INSERT INTO account (name, password) VALUES (#{name}, #{password})")
    void save(@Param("name") String name, @Param("password") String password);

    @Select("SELECT * FROM account WHERE id = #{id}")
    Account findById(@Param("id") Integer id);

    @Select("SELECT * FROM account WHERE name = #{name}")
    Account findByName(@Param("name") String name);

    @Select("SELECT * FROM account WHERE status = 'OK' AND role = 'STUDENT' LIMIT #{offset},#{rowCount}")
    List<Account> findAllStudent(@Param("offset") Integer offset, @Param("rowCount") Integer rowCount);

    @Select("SELECT * FROM account WHERE status = 'OK' AND role = 'STUDENT' AND name LIKE #{nameKeyword} LIMIT #{offset},#{rowCount}")
    List<Account> findAllStudentByName(@Param("offset") Integer offset, @Param("rowCount") Integer rowCount, @Param("nameKeyword") String nameKeyword);
}
