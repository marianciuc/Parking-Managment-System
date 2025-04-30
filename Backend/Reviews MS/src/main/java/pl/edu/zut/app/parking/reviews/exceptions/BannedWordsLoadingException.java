package pl.edu.zut.app.parking.reviews.exceptions;

public class BannedWordsLoadingException extends RuntimeException {
    public BannedWordsLoadingException(String message, Object o) {
        super(message);
    }
}
