package pl.edu.zut.app.parking.auth.exceptions;

public class InvalidSearchCriteriaException extends SecurityServiceException{
    public InvalidSearchCriteriaException(String message) {
        super(message);
    }
}
