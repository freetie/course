package com.github.freetie.course.controller;

import com.github.freetie.course.entity.Order;
import com.github.freetie.course.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/course/order")
    public List<Order> queryOrdersByAccountId(@RequestParam("account-id") Integer accountId) {
        return orderService.queryAllByAccountId(accountId);
    }

    @PostMapping("/course/order")
    public void placeOrder(@RequestBody Order order) {
        orderService.create(order);
    }
}
