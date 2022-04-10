package com.github.freetie.course.service;

import com.github.freetie.course.dao.CourseDao;
import com.github.freetie.course.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    CourseDao courseDao;

    public void create(Course course) {
        courseDao.save(course);
    }

    public void delete(Integer id) {
        courseDao.deleteById(id);
    }

    public void update(Course course) {
        courseDao.update(course);
    }

    public List<Course> findAll() {
        return courseDao.findAll();
    }
}
