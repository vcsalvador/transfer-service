package me.vcs.transferservice.model;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountInfo {
  private Integer accountId;
  private BigDecimal balance;

  public AccountInfo(Integer accountId, BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, balance);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AccountInfo) {
      return Objects.deepEquals(this, obj);
    } else {
      return false;
    }
  }
}
