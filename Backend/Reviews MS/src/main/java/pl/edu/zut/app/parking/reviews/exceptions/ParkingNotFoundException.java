package pl.edu.zut.app.parking.reviews.exceptions;

public class ParkingNotFoundException extends ReviewNotFoundException {
    public ParkingNotFoundException(String message) {
        super(message);
    }
}
