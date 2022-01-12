package com.springboot.bankapp1.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankapp1.dto.Transfer;
import com.springboot.bankapp1.model.Transaction;
import com.springboot.bankapp1.service.TransactionService;

@RestController
public class TransactionController {
	
	private TransactionController {
	
	/*
	 * beneficiary (TO) acct no
	 * username: extract (FROM account no)
	 * amount
	 * {
	 * toAccountNumber: "",
	 * username : "",
	 * } :request body
	 * transfer?toAccountNumber=-__&username=__&amount=__:request param
	 * transfer/toAccountNumber/username/amount : path variable
	 */
	@PostMapping("/trasfer")
	public Transaction doTransfer (Principal principal ,@RequestBody Transfer transfer) {
		String username=principal.getName();
		/*
		 * STEP 1:
		 * Fetch details of fromAccount
		 * 1.1 fetch fromAccountNumber from username
		 * 
		 * STEP 2:
		 * 2.1 DEBIT the amount from fromAccountNumber / update the balance
		 * 2.2 CREDIT the amount to toAccountNumber / update the balance
		 * 
		 * STEP 3:
		 * 3.1 insert the entry of transfer in transaction
		 */
		//
		
		
		TransactionService transactionService;
		//1.1
		String fromAccountNumber = 	transactionService.fetchFromAccountNumber(username);
		
		//2.1
		transactionService.updateBalance(fromAccountNumber,transfer.getAmount());
		
		//3.1
		Transaction transaction = new Transaction();
		transaction.setAccountFrom(fromAccountNumber);
		transaction.setAccountTo(transfer.getToAccountNumber());
		transaction.setAmount(transfer.getAmount());
		transaction.setOperationType("TRANSFER");
		transaction.setDateOfTransaction(new date());
		
		return transactionService.saveTransaction(transaction);
		
	}

}
