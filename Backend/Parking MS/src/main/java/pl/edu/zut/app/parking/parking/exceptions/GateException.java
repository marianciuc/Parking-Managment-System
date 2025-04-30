package pl.edu.zut.app.parking.parking.exceptions;

public abstract class GateException extends RuntimeException {
  public GateException(String message) {
    super(message);
  }
}
