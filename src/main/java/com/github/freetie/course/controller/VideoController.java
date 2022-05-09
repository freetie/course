package com.github.freetie.course.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.entity.Role;
import com.github.freetie.course.entity.Video;
import com.github.freetie.course.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    @Value("${aliyun.access-key-id}")
    private String ACCESS_KEY_ID;
    @Value("${aliyun.access-key-secret}")
    private String ACCESS_KEY_SECRET;
    @Value("${aliyun.endpoing}")
    private String ENDPOINT;
    @Value("${aliyun.bucket}")
    private String BUCKET;

    VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @RoleControl(Role.TEACHER)
    @PostMapping("/course/{id}/video")
    public void addVideo(@PathVariable("id") Integer courseId, @RequestBody Video video) {
        video.setCourseId(courseId);
        videoService.create(video);
    }

    @RoleControl(Role.TEACHER)
    @DeleteMapping("course/{courseId}/video/{videoId}")
    public void deleteVideo(
            HttpServletResponse response,
            @PathVariable("courseId") Integer courseId,
            @PathVariable("videoId") Integer videoId
    ) {
        Video video = videoService.getById(videoId);
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ossClient.deleteObject(BUCKET, video.getPath());
        ossClient.shutdown();
        Video nextVideo = videoService.getByCourseIdAndIndex(courseId, video.getIndex() + 1);
        if (nextVideo != null) {
            videoService.changeIndex(courseId, nextVideo.getId(), "forward");
        }
        videoService.deleteById(videoId);
        response.setStatus(202);
    }

    @RoleControl(Role.TEACHER)
    @PatchMapping("/course/{courseId}/video/{videoId}/index")
    public void changeIndex(
            @PathVariable("courseId") Integer courseId,
            @PathVariable("videoId") Integer videoId,
            @RequestBody() Map<String, String> body
    ) {
        videoService.changeIndex(courseId, videoId, body.get("direction"));
    }

    @GetMapping("/course/{id}/video")
    public List<Video> getVideos(@PathVariable("id") Integer courseId) {
        return videoService.getByCourseId(courseId);
    }

    @GetMapping("/video/{id}/url")
    public URL getVideoUrl(@PathVariable("id") Integer videoId) {
        Video video = videoService.getById(videoId);
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(BUCKET, video.getPath(), expiration);
        ossClient.shutdown();
        return url;
    }

    @RoleControl(Role.TEACHER)
    @GetMapping("/course/{courseId}/signature")
    public VideoUploadSignature getUploadSignature(@PathVariable("courseId") Integer courseId) {
        String host = "http://" + BUCKET + "." + ENDPOINT; // host的格式为 bucketname.endpoint
        String dir = "course-" + courseId + "/"; // 用户上传文件时指定的前缀。
        OSSClient client = new OSSClient(ENDPOINT, new DefaultCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET), null);
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = client.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = client.calculatePostSignature(postPolicy);
        client.shutdown();

        VideoUploadSignature signature = new VideoUploadSignature();
        signature.setAccessid(ACCESS_KEY_ID);
        signature.setPolicy(encodedPolicy);
        signature.setSignature(postSignature);
        signature.setDir(dir);
        signature.setHost(host);
        signature.setExpire(expireEndTime / 1000);

        return signature;
    }

    public static class VideoUploadSignature {
        private String accessid;
        private String policy;
        private String signature;
        private String dir;
        private String host;
        private long expire;

        public String getAccessid() {
            return accessid;
        }

        public void setAccessid(String accessid) {
            this.accessid = accessid;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }
    }
}
