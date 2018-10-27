package me.vcsalvador.transferservice.handler;

import me.vcsalvador.transferservice.exception.TransferOperationException;
import me.vcsalvador.transferservice.model.TransferOperation;
import me.vcsalvador.transferservice.repository.BankingAccount;

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
