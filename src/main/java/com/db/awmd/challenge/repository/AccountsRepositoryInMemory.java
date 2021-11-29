package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.UserTransaction;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

  @Override
  public List<Account> transferAmount(UserTransaction transaction) {
	
	List<Account> accountList = new ArrayList<>();  
	Account accountFrom = accounts.get(transaction.getFromAccountId());
	//Check enough fund in source account to avoid negative balance
	if(accountFrom.getBalance().compareTo(transaction.getAmount())<0) {		
		throw new IllegalArgumentException(
	      "Account id " + accountFrom.getAccountId() + " Not enough Fund from source Account");        
	}
		
	Account accountTo = accounts.get(transaction.getToAccountId());
	
	//Withdraw amount from source Account
	BigDecimal balanceFrom = accountFrom.getBalance().subtract(transaction.getAmount());
	//Add amount in the destination Account
	BigDecimal balanceTo = accountTo.getBalance().add(transaction.getAmount()); 
	
	accountFrom.setBalance(balanceFrom);
	accountTo.setBalance(balanceTo);	
	accountList.add(accountFrom);
	accountList.add(accountTo);	
	  
	//Return both accounts with the new balances
	return accountList;
  }



  
  

//@Override
//public Account transferAmount(String accountNumberFrom, String accountNumberTo, BigDecimal amountToTransfer)
//		throws DuplicateAccountIdException {
//	
//	Account accountFrom = accounts.get(accountNumberFrom);
//	if(accountFrom.getBalance().compareTo(amountToTransfer)<0) {		
//        throw new IllegalArgumentException(
//        		"Account id " + accountFrom.getAccountId() + " NOT ENOUGH BALANCE");        
//	}
//	
//	Account accountTo = accounts.get(accountNumberTo);	
//	BigDecimal balanceFrom = accountFrom.getBalance().subtract(amountToTransfer); //withdrawAmount	
//	BigDecimal balanceTo = accountTo.getBalance().add(amountToTransfer); //addAmount
//	
//	
//	
//}

}
