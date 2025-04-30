package pl.edu.zut.app.parking.reviews.exceptions;

public class IllegalReviewOperation extends ReviewServiceException {
    public IllegalReviewOperation(String message) {
        super(message);
    }
}
