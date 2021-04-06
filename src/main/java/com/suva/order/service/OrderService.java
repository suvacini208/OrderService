package com.suva.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.suva.order.domain.DbSequence;
import com.suva.order.domain.Inventory;
import com.suva.order.domain.Item;
import com.suva.order.domain.Order;
import com.suva.order.repository.DbSequenceRepository;
import com.suva.order.repository.OrderRepository;

@Service
public class OrderService {
	
	private static final Object INVENTORY_TOPIC = "update_inventory";

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	DbSequenceRepository dbSeqRepo;
	
	@Autowired
	private KafkaTemplate<Long, Inventory> kafkaTemplate;
	
	public Order createOrder(Order order) {
		order.setId(getSequence());
		Order newOrder = orderRepository.save(order);
		updateInventoryTopic(newOrder);
		return newOrder;
	}
	
	@Async
	public void updateInventoryTopic(Order order) {
		Inventory inventory = getInventory(order);
        Message<Inventory> message = MessageBuilder
                .withPayload(inventory)
                .setHeader(KafkaHeaders.TOPIC, INVENTORY_TOPIC)
                .build();
        
        kafkaTemplate.send(message);
		System.out.println(String.format("Messent sent to %s successfully", INVENTORY_TOPIC));
	}

	private Inventory getInventory(Order order) {
		Inventory inventory = new Inventory();
		inventory.setId(order.getId());
		inventory.setStatus(order.getStatus());
		inventory.setItem(order.getItem());
		return inventory;
	}
	
	
	/**
	 * Generate the id sequence of cart and update the sequence table
	 * 
	 * @return id
	 */
	private long getSequence() {
		DbSequence dbSeq = dbSeqRepo.findById(Order.DB_SEQ).orElse(new DbSequence());
		long seqId = dbSeq.getSeq() + 1;
		dbSeq.setSeq(seqId);
		dbSeq.setId(Item.DB_SEQ);
		dbSeqRepo.save(dbSeq);

		return seqId;
	}

}
