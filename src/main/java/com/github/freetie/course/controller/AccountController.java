package com.github.freetie.course.controller;

import com.github.freetie.course.entity.Result;
import com.github.freetie.course.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountController {
    AccountService accountService;

    @PostMapping("/account")
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        accountService.save(username, password);
        return null;
    }
}
