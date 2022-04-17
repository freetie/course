package com.github.freetie.course.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.github.freetie.course.entity.Video;
import com.github.freetie.course.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/course/{id}/video")
    public void addVideo(@PathVariable("id") Integer courseId, @RequestBody Video video) {
        video.setCourseId(courseId);
        videoService.create(video);
    }

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
        return videoService.queryVideosByCourseId(courseId);
    }

    @GetMapping("/video/{id}/url")
    public URL getVideoUrl(@PathVariable("id") Integer videoId) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "yourAccessKeyId";
        String accessKeySecret = "yourAccessKeySecret";
        // 从STS服务获取的安全令牌（SecurityToken）。
        String securityToken = "yourSecurityToken";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "examplebucket";
        // 填写Object完整路径，例如exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "exampleobject.txt";

        // 从STS服务获取临时访问凭证后，您可以通过临时访问密钥和安全令牌生成OSSClient。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        return ossClient.generatePresignedUrl(bucketName, objectName, expiration);
    }

    @GetMapping("/course/{courseId}/signature")
    public VideoUploadSignature getUploadSignature(@PathVariable("courseId") Integer courseId) {
        String accessId = ACCESS_KEY_ID; // 请填写您的AccessKeyId。
        String accessKey = ACCESS_KEY_SECRET; // 请填写您的AccessKeySecret。
        String endpoint = ENDPOINT; // 请填写您的 endpoint。
        String bucket = BUCKET; // 请填写您的 bucketname 。
        String host = "http://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        String dir = "course-" + courseId + "/"; // 用户上传文件时指定的前缀。
        OSSClient client = new OSSClient(endpoint, new DefaultCredentialProvider(accessId, accessKey), null);
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

        VideoUploadSignature signature = new VideoUploadSignature();
        signature.setAccessid(accessId);
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
