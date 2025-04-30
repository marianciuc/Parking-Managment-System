package pl.edu.zut.app.parking.admin_ms.exception;

public abstract class AdministratorServceException extends RuntimeException {
  protected AdministratorServceException(String message) {
    super(message);
  }
}
