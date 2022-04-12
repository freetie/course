package com.github.freetie.course.service;

import com.github.freetie.course.dao.OrderDao;
import com.github.freetie.course.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void create(Order order) {
        orderDao.save(order);
    }

    public List<Order> queryAllByAccountId(Integer accountId) {
        return orderDao.findAllByAccountId(accountId);
    }
}
