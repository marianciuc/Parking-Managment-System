package pl.edu.zut.app.parking.payments_ms.exceptions;

public class ParkingNotFoundException extends Exception {
  public ParkingNotFoundException(String message, Exception e) {
    super(message);
    e.fillInStackTrace();
  }

  public ParkingNotFoundException(String parkingNotFound) {
    super(parkingNotFound);
  }
}
