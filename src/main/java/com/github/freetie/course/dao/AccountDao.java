package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountDao {
    @Insert("INSERT INTO account (username, password) VALUES (#{username}, #{password})")
    void save(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM account WHERE id = #{id}")
    Account findById(@Param("id") Integer id);

    @Select("SELECT * FROM account WHERE username = #{username}")
    Account findByUsername(@Param("username") String username);

    @Select("SELECT * FROM account WHERE status = 'OK' AND role = 'STUDENT' LIMIT #{offset},#{rowCount}")
    List<Account> findAllStudent(@Param("offset") Integer offset, @Param("rowCount") Integer rowCount);

    @Select("SELECT * FROM account WHERE status = 'OK' AND role = 'STUDENT' AND username LIKE #{username} LIMIT #{offset},#{rowCount}")
    List<Account> findAllStudentByUsername(@Param("offset") Integer offset, @Param("rowCount") Integer rowCount, @Param("username") String username);

    @Select("SELECT COUNT(*) FROM account WHERE status = 'OK' AND role = 'STUDENT'")
    Integer countStudent();

    @Select("SELECT COUNT(*) FROM account WHERE status = 'OK' AND role = 'STUDENT' AND username LIKE #{username}")
    Integer countStudentByUsername(@Param("username") String username);

    @Delete("UPDATE account SET status = 'DELETED' WHERE id = #{id}")
    void deleteById(@Param("id") Integer id);
}
