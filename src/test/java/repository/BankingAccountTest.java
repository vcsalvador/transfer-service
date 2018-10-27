package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.TransferOperationException;
import java.math.BigDecimal;
import model.TransferOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Test for banking accounts operations")
class BankingAccountTest {

  private BankingAccount bankingAccount;

  @BeforeEach
  void init() {
    bankingAccount = BankingAccount.getInstance();
  }

  @AfterEach
  void tearDown() {
    bankingAccount.clearAccounts();
  }

  @Nested
  @DisplayName("When account is present")
  class WhenAccountIsPresent {

    @BeforeEach
    void init() {
      bankingAccount.createAccount(1, BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("Should have positive balance")
    void shouldHaveBalance() {
      assertNotNull(bankingAccount.getAccountBalance(1));
    }

    @Test
    @DisplayName("Should not change balance")
    void shouldNotChangeBalance() {
      BigDecimal expected = BigDecimal.valueOf(100.00);
      bankingAccount.createAccount(1, BigDecimal.valueOf(200.00));
      assertEquals(expected, bankingAccount.getAccountBalance(1));
    }
  }

  @Nested
  @DisplayName("When executeTransferOperation is required")
  class WhenTransferRequired {
    @BeforeEach
    void setup() {
      bankingAccount.createAccount(1, BigDecimal.valueOf(1000.00));
      bankingAccount.createAccount(2, BigDecimal.valueOf(500.00));
      bankingAccount.createAccount(3, BigDecimal.valueOf(250.00));
    }

    @Test
    @DisplayName("Should not executeTransferOperation more than balance available")
    void transferMoreThanBalanceAvailable() {
      assertThrows(
          TransferOperationException.class,
          () -> bankingAccount.executeTransferOperation(3, BigDecimal.valueOf(300.00), 1));
    }

    @Test
    @DisplayName("Should return a new executeTransferOperation operation")
    void transferSuccessful() {
      TransferOperation expectedOperation = new TransferOperation(1, BigDecimal.valueOf(100.00), 3);
      TransferOperation operation = bankingAccount.executeTransferOperation(1, BigDecimal.valueOf(100.00), 3);

      assertEquals(expectedOperation, operation);
    }

    @Test
    @DisplayName("Should reserve value of executeTransferOperation in executeTransferOperation operation")
    void transferSuccessfulCheckBalance() {
      BigDecimal transferValue = BigDecimal.valueOf(100.00);
      bankingAccount.executeTransferOperation(1, transferValue, 2);
      assertEquals(
          BigDecimal.valueOf(1000.00).subtract(transferValue),
          bankingAccount.getAccountBalance(1));
    }
  }

  @Nested
  @DisplayName("When adding to account")
  class WhenAddingBalanceToAccount {
    @BeforeEach
    void setUp() {
      bankingAccount.createAccount(1, BigDecimal.valueOf(100.00));
    }

    @Test
    @DisplayName("Sould add to balance")
    void shouldAddBalance() {
      bankingAccount.addBalance(1, BigDecimal.valueOf(100.00));
      assertEquals(BigDecimal.valueOf(200.00), bankingAccount.getAccountBalance(1));
    }

    @Test
    @DisplayName("Should throw exception for invalid account")
    void shouldThrowException() {
      assertThrows(
          TransferOperationException.class,
          () -> bankingAccount.addBalance(2, BigDecimal.valueOf(100.00)));
    }
  }
}
