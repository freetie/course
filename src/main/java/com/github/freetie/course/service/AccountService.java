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

    public void save(String name, String password) {
        accountDao.save(name, passwordEncoder.encode(password));
    }

    public Account findByNameAndPassword(String name, String password) {
        Account account = accountDao.findByName(name);
        if (account == null) return null;
        if (!passwordEncoder.matches(password, account.getPassword())) return null;
        return account;
    }

    public List<Account> findAll(Integer page, Integer size, String nameKeyword) {
        int offset = (page - 1) * size;
        if (nameKeyword == null) {
            return accountDao.findAll(offset, size);
        }
        return accountDao.findAllByName(offset, size, nameKeyword);
    }
}
