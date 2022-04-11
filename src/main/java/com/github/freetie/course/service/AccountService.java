package com.github.freetie.course.service;

import com.github.freetie.course.dao.AccountDao;
import com.github.freetie.course.entity.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    PasswordEncoder passwordEncoder;
    AccountDao accountDao;

    public AccountService(PasswordEncoder passwordEncoder, AccountDao accountDao) {
        this.passwordEncoder = passwordEncoder;
        this.accountDao = accountDao;
    }

    public void create(String username, String password) {
        accountDao.save(username, passwordEncoder.encode(password));
    }

    public Account findByNameAndPassword(String username, String password) {
        Account account = accountDao.findByUsername(username);
        if (account == null) return null;
        if (!passwordEncoder.matches(password, account.getPassword())) return null;
        return account;
    }

    public Integer countStudent(String username) {
        if (username == null || username.equals("")) {
            return accountDao.countStudent();
        }
        return accountDao.countStudentByUsername("%" + username + "%");
    }

    public List<Account> findAllStudent(Integer page, Integer size, String username) {
        int offset = (page - 1) * size;
        if (username == null || username.equals("")) {
            return accountDao.findAllStudent(offset, size);
        }
        return accountDao.findAllStudentByUsername(offset, size, "%" + username + "%");
    }

    public void deleteAccount(Integer id) {
        accountDao.deleteById(id);
    }
}
