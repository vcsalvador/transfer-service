package me.vcs.transferservice.repository;

import me.vcs.transferservice.exception.TransferOperationException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import me.vcs.transferservice.model.AccountInfo;
import me.vcs.transferservice.model.TransferOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankingAccount {

  private static final Logger log = LoggerFactory.getLogger(BankingAccount.class);

  private static BankingAccount instance = new BankingAccount();

  private ConcurrentHashMap<Integer, BigDecimal> accounts;

  private BankingAccount() {
    accounts = new ConcurrentHashMap<>();
  }

  /**
   * Singleton instance for {@code BankingAccount}
   *
   * @return {@code BankingAccount} JVM instance or create new synchronized instance
   */
  public static synchronized BankingAccount getInstance() {
    if (instance == null) {
      log.info("Creating BankingAccount instance");
      instance = new BankingAccount();
    }
    return instance;
  }

  public BigDecimal getAccountBalance(Integer accountId) {
    return accounts.get(accountId);
  }

  public Set<AccountInfo> getAllAccounts() {
    return accounts
        .entrySet()
        .stream()
        .map(entry -> new AccountInfo(entry.getKey(), entry.getValue()))
        .collect(Collectors.toSet());
  }

  public BigDecimal createAccount(Integer accountId, BigDecimal balance) {
    return accounts.putIfAbsent(accountId, balance);
  }

  public TransferOperation executeTransferOperation(
      Integer debitAccount, BigDecimal value, Integer creditAccount) {
    accounts.compute(debitAccount, computeTransferOperation(value));
    return new TransferOperation(debitAccount, value, creditAccount);
  }

  public void addBalance(Integer account, BigDecimal value) {
    accounts.compute(account, executeAddBalanceOperation(value));
  }

  private BiFunction<Integer, BigDecimal, BigDecimal> computeTransferOperation(BigDecimal value) {
    return (k, v) -> {
      if (v != null && v.compareTo(value) > -1) {
        return v.subtract(value);
      } else {
        log.error("Account does not have enough balance");
        throw new TransferOperationException("Balance is not enough!");
      }
    };
  }

  private BiFunction<Integer, BigDecimal, BigDecimal> executeAddBalanceOperation(BigDecimal value) {
    return (k, v) -> {
      if (v != null) {
        return v.add(value);
      } else {
        log.error("Could not transfer value to account!");
        throw new TransferOperationException("Could not transfer value to account!");
      }
    };
  }

  public void clearAccounts() {
    accounts.clear();
  }
}
