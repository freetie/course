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

    public void create(String name, String password) {
        accountDao.save(name, passwordEncoder.encode(password));
    }

    public Account findByNameAndPassword(String name, String password) {
        Account account = accountDao.findByName(name);
        if (account == null) return null;
        if (!passwordEncoder.matches(password, account.getPassword())) return null;
        return account;
    }

    public List<Account> findAllStudent(Integer page, Integer size, String nameKeyword) {
        int offset = (page - 1) * size;
        if (nameKeyword == null) {
            return accountDao.findAllStudent(offset, size);
        }
        return accountDao.findAllStudentByName(offset, size, nameKeyword);
    }
}
