package com.shopingapplication.orderservice.repository;

import com.shopingapplication.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> { //Type is Order and Primary key type is Long
}
