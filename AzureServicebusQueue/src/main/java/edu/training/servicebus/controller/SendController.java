package edu.training.servicebus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.training.servicebus.model.Customer;

@RestController
public class SendController {
	private static final String QUEUE_NAME = "customers";

	private static final Logger logger = LoggerFactory.getLogger(SendController.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@PostMapping("add/customer")
	public String postMessage(@RequestBody Customer customer) {
		jmsTemplate.convertAndSend(QUEUE_NAME, customer);
		logger.info("Message : " +customer.getName() + " sent successfully");
		return "message added to queue";
	}
}