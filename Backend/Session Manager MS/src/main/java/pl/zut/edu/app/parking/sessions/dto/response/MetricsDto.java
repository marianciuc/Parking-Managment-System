package pl.zut.edu.app.parking.sessions.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public record MetricsDto(
        Integer activeSessions,
        Double averageTimeOfActiveSessions,
        Integer totalSessionsForWeek,
        Integer totalSessionsForMonth,
        Integer totalSessionsForYear,
        Integer totalSessionsForAllTime,
        List<TopVehicleStatsDto> personalTariffProposition,
        List<PeakHours> peakHours,
        List<TopVehiclesInPeakHoursDto> topVehiclesInPeakHours
) {
}
