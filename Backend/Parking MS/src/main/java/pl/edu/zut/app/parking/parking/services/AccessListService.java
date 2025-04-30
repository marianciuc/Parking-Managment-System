package pl.edu.zut.app.parking.parking.services;

import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.parking.exceptions.AccessListNotFoundException;

import java.util.UUID;

public interface AccessListService <T>{

    /**
     * Adds a new entry to the access list for the specified parking.
     *
     * @param parkingId the unique identifier of the parking
     * @param dto the data transfer object representing the details of the access list entry to be added
     * @return the added entry as an instance of the specified type
     * @throws AccessListNotFoundException if the parking with the provided identifier does not exist
     */
    T add(UUID parkingId, T dto);

    /**
     * Deletes an access list entry identified by the specified unique identifier.
     *
     * @param accessListId the unique identifier of the access list entry to be deleted
     * @throws AccessListNotFoundException if no access list entry with the provided identifier exists
     */
    void delete(UUID accessListId);

    /**
     * Searches for entries in the access list based on the provided parameters.
     *
     * @param parkingId the unique identifier of the parking
     * @param page the page number to retrieve (used for pagination)
     * @param size the number of items per page (used for pagination)
     * @param vehiclePlate the license plate of the vehicle to search for
     * @param accessListId the unique identifier of the access list entry to filter by
     * @param vehicleId the unique identifier of the vehicle to filter by
     * @return a paginated result of entries matching the specified criteria
     */
    Page<T> find(UUID parkingId, Integer page, Integer size,
                            String vehiclePlate, UUID accessListId, UUID vehicleId);

    /**
     * Checks whether a vehicle is authorized to access a specific parking based on the provided parking ID and vehicle plate.
     *
     * @param parkingId the unique identifier of the parking
     * @param vehiclePlate the license plate of the vehicle to check
     * @return a Boolean value indicating whether the vehicle is authorized to access the parking
     */
    Boolean check(UUID parkingId, String vehiclePlate);
}
