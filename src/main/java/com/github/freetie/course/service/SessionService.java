package com.github.freetie.course.service;

import com.github.freetie.course.dao.SessionDao;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {
    SessionDao sessionDao;

    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public String create(Integer accountId) {
        String token = UUID.randomUUID().toString();
        sessionDao.save(accountId, token);
        return token;
    }

    public void delete(String token) {
        sessionDao.deleteByToken(token);
    }
}
