package com.springboot.bankapp1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bankapp1.model.Help;
//import com.springboot.bankapp.model.Transaction;

public interface HelpRepository extends JpaRepository<Help, Long>  {

}
