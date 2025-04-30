package pl.edu.zut.app.parking.parking.services;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.InvalidOpeningHoursException;
import pl.edu.zut.app.parking.parking.exceptions.MissingOpeningHoursException;
import pl.edu.zut.app.parking.parking.exceptions.OpeningHoursAlreadyExistException;

import java.util.List;
import java.util.UUID;

/**
 * The OpeningHoursService interface provides methods for managing the opening hours of parking entities.
 * It allows for creating, updating, and deleting opening hours for a specific parking entity.
 */
public interface OpeningHoursService {

    /**
     * Creates and stores opening hours for a given parking entity.
     *
     * @param parking the parking entity for which the opening hours will be created
     * @param openingHoursDtoList a list of OpeningHoursDto objects containing the opening hours information,
     *                            such as opening time, closing time, whether the parking is closed, and the day of the week
     * @throws OpeningHoursAlreadyExistException if the parking entity already has opening hours
     * @throws InvalidOpeningHoursException if the opening hours for the parking entity are invalid
     */
    void createOpeningHours(Parking parking, List<OpeningHoursDto> openingHoursDtoList);

    /**
     * Updates the opening hours for a specific parking entity.
     *
     * @param parkingId the unique identifier of the parking whose opening hours are to be updated
     * @param openingHoursDtoList a list of OpeningHoursDto objects containing the updated opening hours information,
     *                             such as opening time, closing time, and the day of the week
     * @throws MissingOpeningHoursException if the opening hours for the parking entity are missing
     * @throws InvalidOpeningHoursException if the opening hours for the parking entity are invalid
     */
    void updateOpeningHours(UUID parkingId, List<OpeningHoursDto> openingHoursDtoList);

    /**
     * Deletes all opening hours associated with the given parking entity.
     * This operation removes any previously stored opening hour records
     * linked to the specified parking object.
     *
     * @param parking the parking entity whose associated opening hours
     *                are to be deleted
     */
    void deleteOpeningHours(Parking parking);

    /**
     * Retrieves the opening hours for a specific parking entity.
     *
     * @param parkingId the unique identifier of the parking entity whose opening hours are to be retrieved
     * @return a list of OpeningHoursDto objects representing the opening hours of the specified parking entity
     * @throws InvalidOpeningHoursException if the opening hours for the parking entity aren't found
     */
    List<OpeningHoursDto> getOpeningHours(UUID parkingId);

    /**
     * Creates 24-hour opening hours for a given parking entity.
     * This method updates the parking entity to set it as 24-hour accessible
     * parking by managing its associated opening hours.
     *
     * @param parking the parking entity for which 24-hour opening hours
     *                are to be created
     * @throws OpeningHoursAlreadyExistException if the parking entity already has opening hours
     */
    void create24HourOpeningHours(Parking parking);

    void updateOpeningHours(Parking parking, boolean is24, List<OpeningHoursDto> hours);
}
