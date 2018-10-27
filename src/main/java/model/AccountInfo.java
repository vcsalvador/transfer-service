package model;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountInfo {
  private Integer accountId;
  private BigDecimal balance;

  public AccountInfo(Integer key, BigDecimal value) {
    this.accountId = key;
    this.balance = value;
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
