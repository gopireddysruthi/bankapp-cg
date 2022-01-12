package com.springboot.bankapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankapp1.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
