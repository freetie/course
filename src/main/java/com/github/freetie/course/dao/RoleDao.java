package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDao {
    @Select("SELECT role_id as id, name, created_at, updated_at FROM account_role WHERE account_id = #{accountId} JOIN role on account_role.role_id = role.id")
    List<Role> findAllByAccountId(@Param("accountId") Integer accountId);
}
