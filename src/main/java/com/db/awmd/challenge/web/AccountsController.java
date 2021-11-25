package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.UserTransaction;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

import java.util.List;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;  
  private final NotificationService notificationService;

  @Autowired
  public AccountsController(NotificationService notificationService, AccountsService accountsService) {
    this.accountsService = accountsService;
	this.notificationService = notificationService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }
  
  @PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> transferFund(@RequestBody @Valid UserTransaction transaction) {
	  
	  List<Account> accountList = this.accountsService.transferAmount(transaction);
	  
	  if (!accountList.isEmpty()) {		  
		  for(Account account: accountList) {
			  notificationService.notifyAboutTransfer(account, "Amount transferred: " + transaction.getAmount() + " Actual Balance: " + account.getBalance());			  
		  }		  		 
		  return new ResponseEntity<>(HttpStatus.OK);		  
	  }
	  
	  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  
	  
  }

}
