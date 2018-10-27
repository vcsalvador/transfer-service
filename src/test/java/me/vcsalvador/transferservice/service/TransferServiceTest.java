package me.vcsalvador.transferservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import me.vcsalvador.transferservice.repository.BankingAccount;

class TransferServiceTest {

  private TransferService service;
  private BankingAccount bankingAccount;

  @BeforeEach
  void setUp() {
    service = TransferService.getInstance();
    bankingAccount = BankingAccount.getInstance();
  }

  @Nested
  @DisplayName("When executeTransferOperation is requested")
  class WhenTransferRequested {

    @BeforeEach
    void setUp() {
      bankingAccount.createAccount(1, BigDecimal.valueOf(200.00));
      bankingAccount.createAccount(2, BigDecimal.valueOf(200.00));
    }

    @Test
    @DisplayName("Should produce async executeTransferOperation")
    void shouldProduceAsyncTransfer() {
      service.transfer(1, BigDecimal.valueOf(100.00), 2);
      assertEquals(BigDecimal.valueOf(100.00), bankingAccount.getAccountBalance(1));
      assertEquals(BigDecimal.valueOf(300.00), bankingAccount.getAccountBalance(2));
    }

    @Test
    @DisplayName("Should preserve value from debit account if error")
    void shouldPreserveValueInDebitAccount() {
      service.transfer(1, BigDecimal.valueOf(100.00), 3);
      assertEquals(BigDecimal.valueOf(200.00), bankingAccount.getAccountBalance(1));
    }
  }
}
