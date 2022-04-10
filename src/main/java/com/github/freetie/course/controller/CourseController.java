package com.github.freetie.course.controller;

import com.github.freetie.course.entity.Course;
import com.github.freetie.course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    CourseService courseService;

    @GetMapping("/course")
    public List<Course> queryCourses() {
        return courseService.findAll();
    }

    @PostMapping("/course")
    public void createCourse(@RequestBody Course course) {
        courseService.create(course);
    }

    @PutMapping("/course")
    public void updateCourse(@RequestBody Course course) {
        courseService.update(course);
    }
}
