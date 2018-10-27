package me.vcsalvador.transferservice.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferOperation {
  private Integer debitAccount;
  private BigDecimal value;
  private Integer creditAccount;

  public TransferOperation(Integer debitAccount, BigDecimal value, Integer creditAccount) {
    this.debitAccount = debitAccount;
    this.value = value;
    this.creditAccount = creditAccount;
  }

  public Integer getDebitAccount() {
    return debitAccount;
  }

  public void setDebitAccount(Integer debitAccount) {
    this.debitAccount = debitAccount;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Integer getCreditAccount() {
    return creditAccount;
  }

  public void setCreditAccount(Integer creditAccount) {
    this.creditAccount = creditAccount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(debitAccount, value, creditAccount);
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TransferOperation) {
      return this.debitAccount.equals(((TransferOperation) obj).debitAccount)
          && this.value.equals(((TransferOperation) obj).value)
          && this.creditAccount.equals(((TransferOperation) obj).creditAccount);
    } else {
      return false;
    }
  }
}
