package me.vcs.transferservice.service;

import me.vcs.transferservice.handler.TransferOperationHandler;
import java.math.BigDecimal;
import me.vcs.transferservice.model.TransferOperation;
import me.vcs.transferservice.repository.BankingAccount;

public class TransferService {

  private BankingAccount bankingAccount;

  private static TransferService instance;

  private TransferService() {
    this.bankingAccount = BankingAccount.getInstance();
  }

  /**
   * Singleton instance for {@code TransferService}
   *
   * @return {@code TransferService} JVM instance or create new synchronized instance
   */
  public static synchronized TransferService getInstance() {
    if (instance == null) {
      instance = new TransferService();
    }
    return instance;
  }

  public void transfer(Integer debitAccount, BigDecimal value, Integer creditAccount) {
    TransferOperation operation =
        bankingAccount.executeTransferOperation(debitAccount, value, creditAccount);
    TransferOperationHandler.getInstance(operation).run();
  }
}
