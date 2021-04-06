package com.suva.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.suva.order.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
