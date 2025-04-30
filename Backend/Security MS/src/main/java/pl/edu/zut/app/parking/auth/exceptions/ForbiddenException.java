package pl.edu.zut.app.parking.auth.exceptions;

public class ForbiddenException extends SecurityServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
