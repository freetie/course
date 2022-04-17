package com.github.freetie.course.controller;

import com.github.freetie.course.entity.Course;
import com.github.freetie.course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public List<Course> queryCourses() {
        return courseService.findAll();
    }

    @PostMapping("/course")
    public void createCourse(@RequestBody Course course) {
        courseService.create(course);
    }

    @PutMapping("/course/{id}")
    public void updateCourse(@PathVariable("id") Integer id, @RequestBody Course course) {
        course.setId(id);
        courseService.update(course);
    }
}
