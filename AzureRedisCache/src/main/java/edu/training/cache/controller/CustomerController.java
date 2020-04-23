package edu.training.cache.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.training.cache.model.Customer;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
	@Autowired
	private StringRedisTemplate template;

	@PostMapping("add")
	public String add(@RequestBody Customer customer) {
		ObjectMapper objectMapper = new ObjectMapper();
		UUID uniqueId = UUID.randomUUID();
		customer.setKey(uniqueId.toString());
		try {
			String customerJson = objectMapper.writeValueAsString(customer);
			ValueOperations<String, String> operation = this.template.opsForValue();
			if (!this.template.hasKey(customer.getKey())) {
				operation.set(customer.getKey(), customerJson);
			}
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}
		return "Customer added successfully to cache with key : " + customer.getKey();
	}

	@PostMapping("get")
	public Customer get(@RequestBody Customer customer) {		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ValueOperations<String, String> operation = this.template.opsForValue();
			customer = objectMapper.readValue(operation.get(customer.getKey()), Customer.class);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
		}
		return customer;
	}
}
