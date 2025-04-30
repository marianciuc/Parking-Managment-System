package pl.edu.zut.app.parking.parking.exceptions;

import pl.edu.zut.app.parking.parking.entities.OpeningHours;

public class MissingDayOpeningHoursException extends ParkingException {
    public MissingDayOpeningHoursException(String day) {
        super("No opening hours provided for day: " + day);
    }
}
