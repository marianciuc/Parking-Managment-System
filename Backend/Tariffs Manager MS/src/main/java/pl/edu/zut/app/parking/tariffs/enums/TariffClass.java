package pl.edu.zut.app.parking.tariffs.enums;

import lombok.Getter;

@Getter
public enum TariffClass {
    FOR_15_MINUTES(15, TariffType.TIME_BASED),
    FOR_30_MINUTES(30, TariffType.TIME_BASED),
    FOR_1_HOUR(60, TariffType.TIME_BASED),
    FOR_WHITELISTED(-1, TariffType.ACCESS_CONTROLLED),
    FOR_1_DAY(1440, TariffType.TIME_BASED);

    private final int minutes;
    private final TariffType type;

    TariffClass(int minutes, TariffType type) {
        this.minutes = minutes;
        this.type = type;
    }
}