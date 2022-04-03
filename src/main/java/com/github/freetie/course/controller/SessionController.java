package com.github.freetie.course.controller;

import com.github.freetie.course.configuration.ApplicationConfiguration;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.HttpException;
import com.github.freetie.course.service.AccountService;
import com.github.freetie.course.service.SessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.github.freetie.course.configuration.ApplicationConfiguration.AccountInterceptor.COOKIE_TOKEN_NAME;

@RestController
public class SessionController {
    AccountService accountService;
    SessionService sessionService;

    @PostMapping("/session")
    public Account login(@RequestBody Map<String, String> usernameAndPassword, HttpServletResponse response) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        Account account = accountService.findByNameAndPassword(username, password);
        if (account == null) {
            throw new HttpException(400, "Bad Request");
        }
        String token = sessionService.createSession(account.getId());
        response.addCookie(new Cookie(COOKIE_TOKEN_NAME, token));
        return account;
    }

    @GetMapping("/session")
    public Account auth() {
        Account currentAccount = ApplicationConfiguration.AccountContext.getCurrentAccount();
        if (currentAccount == null) {
            throw new HttpException(401, "Unauthorized");
        }
        return currentAccount;
    }
}
