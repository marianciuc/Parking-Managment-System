package pl.edu.zut.app.parking.auth.exceptions;

public class AccountNotSupportedLoginByCredentialsException extends RuntimeException {
  public AccountNotSupportedLoginByCredentialsException() {
    super("Account does not support login by credentials");
  }
}
