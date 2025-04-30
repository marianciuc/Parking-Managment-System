package pl.edu.zut.app.parking.parking.dto.requests;

public record ParkingUpdateRequest(
        String name,
        String imageUrl,
        String description
) {
}
