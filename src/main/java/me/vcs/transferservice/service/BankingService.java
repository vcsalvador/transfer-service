package me.vcs.transferservice.service;

import me.vcs.transferservice.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Set;
import me.vcs.transferservice.model.AccountInfo;
import me.vcs.transferservice.repository.BankingAccount;

public class BankingService {

  private static BankingService instance;

  private BankingAccount bankingAccount;

  private BankingService() {
    bankingAccount = BankingAccount.getInstance();
  }

  /**
   * Singleton synchronized instance of {@code BankingService}
   *
   * @return singleton {@code BankingService.instance}
   */
  public static synchronized BankingService getInstance() {
    if (instance == null) {
      instance = new BankingService();
    }
    return instance;
  }

  public AccountInfo getAccountInfo(Integer account) {
    BigDecimal accountBalance = bankingAccount.getAccountBalance(account);
    if (accountBalance != null) {
      return new AccountInfo(account, accountBalance);
    }
    throw new AccountNotFoundException("Can't find account with id:" + account);
  }

  public Set<AccountInfo> getAllAccountsInfo() {
    return bankingAccount.getAllAccounts();
  }

  public BigDecimal createAccount(Integer account, BigDecimal balance) {
    return bankingAccount.createAccount(account, balance);
  }

  public void deleteAll() {
    bankingAccount.clearAccounts();
  }
}
