package edu.training.servicebus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import edu.training.servicebus.model.Customer;

@Component
public class RecieveController {
    private static final String QUEUE_NAME = "customers";

    private final Logger logger = LoggerFactory.getLogger(RecieveController.class);

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Customer customer) {
        logger.info("Received message: {}", customer.getName());
    }
}
