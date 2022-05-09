package com.github.freetie.course.service;

import com.github.freetie.course.dao.VideoDao;
import com.github.freetie.course.entity.Video;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    VideoDao videoDao;

    public VideoService(VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    public void create(Video video) {
        videoDao.save(video);
    }

    public void deleteById(Integer videoId) {
        videoDao.deleteById(videoId);
    }

    public void deleteByCourseId(Integer courseId) {
        videoDao.deleteByCourseId(courseId);
    }

    public List<Video> getByCourseId(Integer courseId) {
        return videoDao.findByCourseId(courseId);
    }

    public Video getById(Integer id) {
        return videoDao.findById(id);
    }

    public Video getByCourseIdAndIndex(Integer courseId, Integer index) {
        return videoDao.findByCourseIdAndIndex(courseId, index);
    }
    public void changeIndex(Integer courseId, Integer videoId, String direction) {
        Video targetVideo = videoDao.findById(videoId);
        Video videoSwapped = videoDao.findByCourseIdAndIndex(courseId, direction.equals("forward") ? targetVideo.getIndex() - 1 : targetVideo.getIndex() + 1);
        videoDao.updateIndex(videoId, direction.equals("forward") ? targetVideo.getIndex() - 1 : targetVideo.getIndex() + 1);
        videoDao.updateIndex(videoSwapped.getId(), direction.equals("forward") ? videoSwapped.getIndex() + 1 : videoSwapped.getIndex() - 1);
    }
}
