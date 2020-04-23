package edu.training.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.training.database.implementation.CustomerRepository;
import edu.training.database.model.Customer;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping("add")
	public String postMessage(@RequestBody Customer customer) {
		customerRepository.save(customer);
		return "Customer added successfully";
	}
	
	  @GetMapping(path="/all")
	  public @ResponseBody Iterable<Customer> getAllUsers() {
	    return customerRepository.findAll();
	  }	
}
