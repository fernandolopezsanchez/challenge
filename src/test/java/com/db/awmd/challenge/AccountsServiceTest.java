package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.UserTransaction;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  @Test
  public void transferAmount() throws Exception {
    //Given
	UserTransaction userTransaction = new UserTransaction("Id-111", "Id-222", new BigDecimal(300));
	  
	Account account01 = new Account("Id-111");
    account01.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account01);
    
    Account account02 = new Account("Id-222");
    account02.setBalance(new BigDecimal(800));
    this.accountsService.createAccount(account02);    
    
    //When    
    List<Account> accountList = this.accountsService.transferAmount(userTransaction);
    
    //Then
    assertTrue(accountList.size()>1);
    
  }
  
  @Test
  public void transferAmount_failsNotEnoughFund() throws Exception {
    //Given
	UserTransaction userTransaction = new UserTransaction("Id-122", "Id-233", new BigDecimal(300));
	  
	Account account01 = new Account("Id-122");
    account01.setBalance(new BigDecimal(200));
    this.accountsService.createAccount(account01);  
    
    //When 
    try {
    this.accountsService.transferAmount(userTransaction);
    fail("Should have failed when the amount is greater than the balance");
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + account01.getAccountId() + " Not enough Fund from source Account");
      
    }
    
  }  


}
