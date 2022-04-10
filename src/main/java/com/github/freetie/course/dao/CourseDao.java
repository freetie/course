package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseDao {
    @Insert("INSERT INTO course (title, `desc`, picture, price) VALUES (#{title}, #{desc}, #{picture}, #{price})")
    void save(Course course);

    @Select("SELECT * FROM course")
    List<Course> findAll();

    @Update("UPDATE course SET title = #{title}, `desc` = #{desc}, picture = #{picture}, price = #{price} WHERE id = #{id}")
    void update(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    void deleteById(@Param("id") Integer id);
}
