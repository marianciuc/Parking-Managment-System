package pl.edu.zut.app.parking.parking.dto.common;

import pl.edu.zut.app.parking.parking.entities.OpeningHours;

import java.time.LocalTime;

public record OpeningHoursDto(
        boolean isClosed,
        LocalTime openingTime,
        LocalTime closingTime,
        OpeningHours.DayOfWeek dayOfWeek
) {
    public static OpeningHoursDto fromEntity(OpeningHours openingHours) {
        return new OpeningHoursDto(
                openingHours.isClosed(),
                openingHours.getOpeningTime(),
                openingHours.getClosingTime(),
                openingHours.getDayOfWeek()
        );
    }
}
