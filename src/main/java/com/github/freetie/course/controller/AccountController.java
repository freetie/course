package com.github.freetie.course.controller;

import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.Result;
import com.github.freetie.course.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/student")
    public List<Account> queryStudents(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return accountService.findAllStudent(page, size, keyword);
    }

    @PostMapping("/account")
    public Result signup(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        accountService.create(username, password);
        return null;
    }
}
