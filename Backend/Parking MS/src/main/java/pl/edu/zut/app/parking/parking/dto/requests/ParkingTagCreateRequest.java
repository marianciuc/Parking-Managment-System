package pl.edu.zut.app.parking.parking.dto.requests;

public record ParkingTagCreateRequest(
        String name,
        String description
) {
}
