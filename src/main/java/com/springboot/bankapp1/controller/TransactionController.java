package com.springboot.bankapp1.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bankapp1.dto.Transfer;
import com.springboot.bankapp1.model.Account;
import com.springboot.bankapp1.model.Help;
import com.springboot.bankapp1.model.Transaction;
import com.springboot.bankapp1.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	/*
	 * beneficiary(TO) acct no 
	 * username: extract(FROM account no) 
	 * amount 
	 * {  
	 *   toAccountNumber: "", 
	 *   amount: ""  
	 * } : request body 
	 *  transfer?toAccountNumber=___&username=___&amount=__ : request param
	 *  transfer/toAccountNumber/username/amount : path variable 
	 */
	
	
	@PostMapping("/transfer")
	public Transaction doTransfer(Principal principal, @RequestBody Transfer transfer) {
		String username=principal.getName(); 
		System.out.println(username);
		System.out.println(transfer);
		System.out.println("In transfer api....");
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
		 * 3.1 insert the entry of transfer in transaction table 
		 */
		 
		//1.1
		String fromAccountNumber = transactionService.fetchFromAccountNumber(username);
		
		//2.1 
		transactionService.updateBalance(fromAccountNumber, transfer.getAmount());
		//2.2 
		transactionService.creditAmount(transfer.getToAccountNumber(), transfer.getAmount());
		
		//3.1
		Transaction transaction = new Transaction();
		transaction.setAccountFrom(fromAccountNumber);
		transaction.setAccountTo(transfer.getToAccountNumber());
		transaction.setAmount(transfer.getAmount());
		transaction.setOperationType("TRANSFER");
		transaction.setDateOfTransaction(new Date());
		
		return transactionService.saveTransaction(transaction);
		 
		
	}
	@GetMapping("/statement/{startDate}/{endDate}")
	
	public List<Transaction> generateStatement(Principal principal, 
			@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, 
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

		String username=principal.getName(); 

		/* 
		 * Step 1: extract account number based on username
		 */

		/*
		 * Step 2:  
		 * Fetch transactions for above account number
		 * this number should be either in  account_from or account_to
		 * this will give me List<Tansaction>
		 */

		/*
		 * Step 3: 
		 * From List<Transaction> of step-2, I will filter this based on 
		 * startDate and endDate given. 
		 * return this List<Transaction>
		 */

		//Step 1
		String accountNumber = transactionService.fetchFromAccountNumber(username);

		//Step 2
		List<Transaction> list = transactionService.fetchTransactionsByAccountNumber(accountNumber);

		//Step 3
		try {
			//convert LocalDate to Date
			Date startDateToDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate.toString());
			Date endDateToDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate.toString());
			//2022-01-10
			list = list.parallelStream()
					.filter(t-> t.getDateOfTransaction().compareTo(startDateToDate) >= 0)
					.filter(t-> t.getDateOfTransaction().compareTo(endDateToDate) <= 0)
					.collect(Collectors.toList());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return list; 
}
	@GetMapping("/balance")
	public double accountBalance(Principal principal) {
		/*
		 * Balance enquiry
		 * 
		 * step-1
		 * fetch account number based on username
		 */
		
		String username=principal.getName();
		String accountNumber=transactionService.fetchFromAccountNumber(username);
		
		Account account=transactionService.getAccountByAccountNumber(accountNumber);
		return account.getBalance();
	}
	@PostMapping("/help")
	public Help postQnA(@RequestBody Help help) {
		return transactionService.postQnA(help);
	}
	@GetMapping("/help/{id}")
	public Help getQnA(@PathVariable("id") Long id) {
		return transactionService.getQnA(id);
	}
	@PostMapping("/deposit/{amount}")
	public Transaction deDeposit(Principal principal,@PathVariable("amount") double amount) {
		/*
		 * Deposit
		 * 
		 * step-1
		 * fetch account number based on username
		 * 
		 * step-2
		 * update the balance of the user and add the amount to the balance 
		 * 
		 * step-3
		 * add an entry in transaction table 
		 * operation type="DEPOSIT"
		 * account_from=account_to=accountNumber
		 */
		
		//step-1
		String username=principal.getName();
String accountNumber = transactionService.fetchFromAccountNumber(username);
		
		//step-2:
		transactionService.depositAmount(accountNumber, amount);
		
		//step-3:
		Transaction transaction = new Transaction();
		transaction.setAccountFrom(accountNumber);
		transaction.setAccountTo(accountNumber);
		transaction.setAmount(amount);
		transaction.setOperationType("DEPOSIT");
		transaction.setDateOfTransaction(new Date());		
		return transactionService.saveTransaction(transaction);
	}
}
		