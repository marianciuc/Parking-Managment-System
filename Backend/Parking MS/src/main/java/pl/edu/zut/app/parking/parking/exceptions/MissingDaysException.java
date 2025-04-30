package pl.edu.zut.app.parking.parking.exceptions;

import java.util.List;

public class MissingDaysException extends ParkingException {
    public MissingDaysException(List<String> missingDays) {
        super("Some days of the week are missing from the working hours: " + String.join(", ", missingDays));
    }
}