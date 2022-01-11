package com.springboot.bankapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankapp1.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
