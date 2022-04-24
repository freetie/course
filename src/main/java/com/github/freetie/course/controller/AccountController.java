package com.github.freetie.course.controller;

import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.PaginatedResult;
import com.github.freetie.course.entity.Result;
import com.github.freetie.course.entity.Role;
import com.github.freetie.course.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RoleControl(Role.ADMIN)
    @GetMapping("/student")
    public PaginatedResult<Account> queryStudents(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "username", required = false) String username) {
        PaginatedResult<Account> paginatedResult = new PaginatedResult<>();
        paginatedResult.setPage(page);
        paginatedResult.setSize(size);
        paginatedResult.setCount(accountService.countStudent(username));
        paginatedResult.setItems(accountService.findAllStudent(page, size, username));
        return paginatedResult;
    }

    @RoleControl(Role.ADMIN)
    @PostMapping("/account")
    public void signup(HttpServletResponse response, @RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        accountService.create(username, password);
        response.setStatus(201);
    }

    @RoleControl(Role.ADMIN)
    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
    }
}
