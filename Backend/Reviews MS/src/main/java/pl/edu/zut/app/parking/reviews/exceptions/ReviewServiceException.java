package pl.edu.zut.app.parking.reviews.exceptions;

public abstract class ReviewServiceException extends RuntimeException {
    public ReviewServiceException(String message) {
        super(message);
    }
}
