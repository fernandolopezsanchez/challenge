package com.db.awmd.challenge.repository;

import java.util.List;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.UserTransaction;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);
  
  List<Account> transferAmount(UserTransaction transaction);

  void clearAccounts();
  
}
