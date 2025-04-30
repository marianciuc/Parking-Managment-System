package pl.zut.edu.app.parking.sessions.services;

import pl.zut.edu.app.parking.sessions.dto.response.MetricsDto;
import pl.zut.edu.app.parking.sessions.dto.response.PeakHours;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehicleStatsDto;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehiclesInPeakHoursDto;

import java.util.List;
import java.util.UUID;

/**
 * Interface responsible for providing metrics and statistical data of parking sessions.
 * This service handles operations including calculating sessions data, peak usage hours,
 * and generating insights on top vehicles and their usage patterns.
 */
public interface SessionMetricsService {

    /**
     * Retrieves metrics and statistical data for a specific parking area.
     * The metrics include details such as active sessions, average session duration,
     * weekly, monthly, yearly, and all-time session counts, as well as insights into
     * peak hours and top vehicle usage.
     *
     * @param parkingId the unique identifier of the parking area for which metrics are requested
     * @return an instance of MetricsDto containing the calculated metrics for the specified parking area
     */
    MetricsDto getMetricsForParking(UUID parkingId);

    /**
     * Retrieves the number of active parking sessions for a given parking area.
     *
     * @param parkingId the unique identifier of the parking area for which the active session count is requested
     * @return the count of currently active parking sessions for the specified parking area
     */
    Integer getAmountOfActiveSessions(UUID parkingId);
    /**
     * Calculates the average duration of completed parking sessions for a specific parking area.
     * The calculation is based on finished sessions registered in the system.
     *
     * @param parkingId the unique identifier of the parking area for which the average session time is calculated
     * @return the average duration of completed sessions in the specified parking area,
     *         or null if there are no completed sessions
     */
    Double calculateAverageTimeOfSessions(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions that have been completed for a specific parking area.
     * A finished session is defined as one that has been successfully ended.
     *
     * @param parkingId the unique identifier of the parking area for which the count of finished sessions is requested
     * @return the total number of finished parking sessions for the specified parking area
     */
    Integer getAmountOfFinishedSessions(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions that have been cancelled for a specific parking area.
     * A cancelled session is defined as one that was started but subsequently marked as cancelled.
     *
     * @param parkingId the unique identifier of the parking area for which the cancelled session count is requested
     * @return the total number of cancelled parking sessions for the specified parking area
     */
    Integer getAmountOfCancelledSessions(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions for a specific parking area during the current week.
     *
     * @param parkingId the unique identifier of the parking area for which the total weekly session count is requested
     * @return the total number of parking sessions for the specified parking area during the current week
     */
    Integer getTotalSessionsForWeek(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions for a specific parking area during the current month.
     *
     * @param parkingId the unique identifier of the parking area for which the total monthly session count is requested
     * @return the total number of parking sessions for the specified parking area during the current month
     */
    Integer getTotalSessionsForMonth(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions for a specific parking area during the current year.
     *
     * @param parkingId the unique identifier of the parking area for which the total yearly session count is requested
     * @return the total number of parking sessions for the specified parking area during the current year
     */
    Integer getTotalSessionsForYear(UUID parkingId);

    /**
     * Retrieves the total number of parking sessions for a specific parking area
     * for all time.
     *
     * @param parkingId the unique identifier of the parking area for which the total
     *                  session count is requested
     * @return the total number of parking sessions for the specified parking area
     *         for all time
     */
    Integer getTotalSessionsForAllTime(UUID parkingId);

    /**
     * Retrieves personalized tariff propositions for a specific parking area.
     * The propositions are based on vehicle usage statistics, such as average sessions per month.
     *
     * @param parkingId the unique identifier of the parking area for which the tariff propositions are requested
     * @return a list of {@link TopVehicleStatsDto} containing vehicle statistics associated with the specified parking area
     */
    List<TopVehicleStatsDto> getPersonalTariffProposition(UUID parkingId);

    /**
     * Retrieves information about the peak hours for a specific parking area.
     * Peak hours indicate the times of highest parking session activity
     * based on historical data.
     *
     * @param parkingId the unique identifier of the parking area for which peak hour data is requested
     * @return a list of {@code PeakHours} records, each representing a day of the week, an hour of the day,
     *         and the corresponding count of active parking sessions during that time
     */
    List<PeakHours> getPeakHours(UUID parkingId);

    /**
     * Retrieves the vehicles with the highest session counts during peak hours
     * for a specific parking area. This information can help identify vehicles
     * that are most frequently present during times of high parking activity.
     *
     * @param parkingId the unique identifier of the parking area for which
     *                  top vehicles during peak hours are requested
     * @return a list of {@code TopVehiclesInPeakHoursDto} containing details
     *         about vehicles, including their unique identifiers, license plate
     *         numbers, and the count of sessions recorded during peak hours
     */
    List<TopVehiclesInPeakHoursDto> getTopVehiclesInPeakHours(UUID parkingId);
}
