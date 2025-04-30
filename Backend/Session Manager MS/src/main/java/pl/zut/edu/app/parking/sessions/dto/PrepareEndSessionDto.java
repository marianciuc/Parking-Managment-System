package pl.zut.edu.app.parking.sessions.dto;

public record PrepareEndSessionDto(
        Boolean isPaidSuccessfully,
        Payment payment,
        java.util.UUID sessionId
) {

}
