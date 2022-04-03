package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Session;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SessionDao {
    @Insert("INSERT INTO session (account_id, token) VALUES (#{accountId}, #{token})")
    void save(@Param("accountId") Integer accountId, @Param("token") String token);

    @Select("SELECT * FROM session WHERE token = #{token}")
    Session findByToken(@Param("token") String token);
}
