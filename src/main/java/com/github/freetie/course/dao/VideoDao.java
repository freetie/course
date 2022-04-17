package com.github.freetie.course.dao;

import com.github.freetie.course.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoDao {
    @Insert("INSERT INTO video (course_id, `index`, title, path) VALUES (#{courseId}, #{index}, #{title}, #{path})")
    void save(Video video);

    @Select("SELECT * FROM video WHERE course_id=#{courseId} ORDER BY `index`")
    List<Video> findByCourseId(@Param("courseId") Integer courseId);

    @Select("SELECT * FROM video WHERE id=#{id}")
    Video findById(@Param("id") Integer id);

    @Select("SELECT * FROM video WHERE course_id=#{courseId} AND `index`=#{index}")
    Video findByCourseIdAndIndex(@Param("courseId") Integer courseId, @Param("index") Integer index);

    @Update("UPDATE video SET `index`=#{index} WHERE id=#{id}")
    void updateIndex(@Param("id") Integer id, @Param("index") Integer index);
}
