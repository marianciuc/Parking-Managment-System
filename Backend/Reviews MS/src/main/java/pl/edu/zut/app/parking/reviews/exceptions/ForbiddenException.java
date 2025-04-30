package pl.edu.zut.app.parking.reviews.exceptions;

public class ForbiddenException extends ReviewServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
