package pl.zut.edu.app.parking.sessions.dto.response;

public record PeakHours (
        int dayOfWeek,
        int hourOfDay,
        long activeSessions
) {
}
