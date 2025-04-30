package pl.edu.zut.app.parking.parking.enums;

import lombok.Getter;

public enum ParkingStatus {
    TEMPORARY_CLOSED("Temporary closed", false),
    MODERATE("Moderate traffic", false),
    FULL("Full capacity", false),
    CLOSED("Is not working time", true),
    CREATION_PROCESS("Creation process", false),
    OPEN("Open for parking", true),
    ARCHIVED("Archived", false);

    @Getter
    private final String description;
    private final boolean isVisible;

    ParkingStatus(String description, boolean isVisible) {
        this.description = description;
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }
}