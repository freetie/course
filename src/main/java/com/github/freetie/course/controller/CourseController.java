package com.github.freetie.course.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.entity.Course;
import com.github.freetie.course.entity.Role;
import com.github.freetie.course.service.CourseService;
import com.github.freetie.course.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {
    @Value("${aliyun.access-key-id}")
    private String ACCESS_KEY_ID;
    @Value("${aliyun.access-key-secret}")
    private String ACCESS_KEY_SECRET;
    @Value("${aliyun.endpoing}")
    private String ENDPOINT;
    @Value("${aliyun.bucket}")
    private String BUCKET;
    CourseService courseService;
    VideoService videoService;

    public CourseController(CourseService courseService, VideoService videoService) {
        this.courseService = courseService;
        this.videoService = videoService;
    }

    @GetMapping("/course")
    public List<Course> queryCourses() {
        return courseService.findAll();
    }

    @RoleControl(Role.TEACHER)
    @DeleteMapping("/course/{id}")
    public void deleteCourse(HttpServletResponse response, @PathVariable("id") Integer id) {
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
//        ossClient.deleteDirectory(BUCKET, "course-" + id, true);
        final String prefix = "course-" + id + "/";
        String nextMarker = null;
        ObjectListing objectListing;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET)
                    .withPrefix(prefix)
                    .withMarker(nextMarker);

            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    System.out.println("key name: " + s.getKey());
                    keys.add(s.getKey());
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(BUCKET).withKeys(keys).withEncodingType("url");
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                for (String obj : deletedObjects) {
                    String deleteObj = URLDecoder.decode(obj, StandardCharsets.UTF_8);
                    System.out.println(deleteObj);
                }
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        ossClient.shutdown();
        videoService.deleteByCourseId(id);
        courseService.delete(id);
        response.setStatus(202);
    }

    @RoleControl(Role.TEACHER)
    @PostMapping("/course")
    public void createCourse(@RequestBody Course course) {
        courseService.create(course);
    }

    @RoleControl(Role.TEACHER)
    @PutMapping("/course/{id}")
    public void updateCourse(@PathVariable("id") Integer id, @RequestBody Course course) {
        course.setId(id);
        courseService.update(course);
    }
}
