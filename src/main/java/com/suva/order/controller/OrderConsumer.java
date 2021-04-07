package com.suva.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.suva.order.domain.Order;
import com.suva.order.service.OrderService;

@RestController
public class OrderConsumer {
	
	@Autowired
	OrderService orderService;

	@KafkaListener(topics = {"update_order"}, containerFactory = "orderListenerFactory")
	public void consumeAccountBalance(@RequestBody Order order) {
		System.out.println("Kafka event consumed is: " + order.getId());
		orderService.saveOrder(order);
    }
	
}
