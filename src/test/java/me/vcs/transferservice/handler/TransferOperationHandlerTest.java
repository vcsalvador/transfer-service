package me.vcs.transferservice.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import me.vcs.transferservice.model.TransferOperation;
import me.vcs.transferservice.repository.BankingAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Transfer Handler Test")
class TransferOperationHandlerTest {

  private TransferOperationHandler transferOperationHandler;
  private BankingAccount bankingAccount;

  @BeforeEach
  void setUp() {
    bankingAccount = BankingAccount.getInstance();
    bankingAccount.createAccount(1, BigDecimal.valueOf(200.00));
    bankingAccount.createAccount(2, BigDecimal.valueOf(200.00));
  }

  @AfterEach
  void tearDown() {
    bankingAccount.clearAccounts();
  }

  @Test
  @DisplayName("Should transfer between two accounts")
  void shouldTransferAmountBetweenAccounts() {
    TransferOperation operation = new TransferOperation(1, BigDecimal.valueOf(100.00), 2);
    transferOperationHandler = TransferOperationHandler.getInstance(operation);

    transferOperationHandler.run();

    assertEquals(BigDecimal.valueOf(200.00), bankingAccount.getAccountBalance(1));
    assertEquals(BigDecimal.valueOf(300.00), bankingAccount.getAccountBalance(2));
  }

  @Test
  @DisplayName("Should reimburse amount to origin account")
  void shouldReimburseAmountToOriginAccount() {
    TransferOperation operation = new TransferOperation(1, BigDecimal.valueOf(100.00), 3);
    transferOperationHandler = TransferOperationHandler.getInstance(operation);

    transferOperationHandler.run();

    assertEquals(BigDecimal.valueOf(300.00), bankingAccount.getAccountBalance(1));
  }
}