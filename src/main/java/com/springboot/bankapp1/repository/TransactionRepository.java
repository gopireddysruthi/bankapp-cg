package com.springboot.bankapp1.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bankapp1.model.Account;
import com.springboot.bankapp1.model.Help;
import com.springboot.bankapp1.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	/*
	 * Step 2:  
	 * Fetch transactions for above account number
	 * this number should be either in  account_from or account_to
	 * this will give me List<Tansaction>
	 */
	@Query("select t from Transaction t where t.accountFrom = ?1 OR t.accountTo=?1 order by t.dateOfTransaction DESC")
	List<Transaction> fetchTransactionsByAccountNumber(String accountNumber);
	
	@Transactional
	@Modifying
	@Query("update Account a SET a.balance=a.balance+?2 where a.accountNumber=?1")
	void depositAmount(String accountNumber,double amount);
	
	@Query("select a from Account a where a.accountNumber=?1")
	Account getAccountByAccountNumber(String accountNumber);
	
	@Query("select h from Help h where h.id=?1")
	Help getQnA(Long id);
}