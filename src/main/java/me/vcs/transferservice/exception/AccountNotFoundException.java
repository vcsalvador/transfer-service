package me.vcs.transferservice.exception;

public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(String message) {
    super(message);
  }
}
