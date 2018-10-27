package me.vcs.transferservice.handler;

import me.vcs.transferservice.exception.TransferOperationException;
import me.vcs.transferservice.model.TransferOperation;
import me.vcs.transferservice.repository.BankingAccount;

public class TransferOperationHandler implements Runnable {

  private BankingAccount accounts;
  private TransferOperation operation;

  public static TransferOperationHandler getInstance(TransferOperation operation) {
    return new TransferOperationHandler(operation);
  }

  private TransferOperationHandler(TransferOperation transferOperation) {
    accounts = BankingAccount.getInstance();
    operation = transferOperation;
  }

  @Override
  public void run() {
    try {
      accounts.addBalance(operation.getCreditAccount(), operation.getValue());
    } catch (TransferOperationException e) {
      accounts.addBalance(operation.getDebitAccount(), operation.getValue());
    }
  }
}
