package edu.training.database.implementation;

import org.springframework.data.repository.CrudRepository;

import edu.training.database.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{

}
