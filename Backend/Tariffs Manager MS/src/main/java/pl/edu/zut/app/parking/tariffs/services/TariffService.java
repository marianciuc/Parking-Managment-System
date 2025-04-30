package pl.edu.zut.app.parking.tariffs.services;

import pl.edu.zut.app.parking.tariffs.dto.TariffDto;
import pl.edu.zut.app.parking.tariffs.dto.UpdateTariffRequest;
import pl.edu.zut.app.parking.tariffs.exceptions.ExternalServiceException;
import pl.edu.zut.app.parking.tariffs.exceptions.ParkingNotFoundException;
import pl.edu.zut.app.parking.tariffs.exceptions.TariffAlreadyExistsException;
import pl.edu.zut.app.parking.tariffs.exceptions.TariffNotFoundException;
import pl.edu.zut.app.parking.tariffs.kafka.messages.ChangeParkingCurrencyMessage;
import pl.edu.zut.app.parking.tariffs.kafka.messages.CreatedParkingMessage;

import java.util.List;
import java.util.UUID;

/**
 * This interface defines the contract for managing parking tariffs.
 * It provides methods to create, update, delete, and retrieve tariff details
 * as well as associate them with specific parking configurations.
 */
public interface TariffService {

    /**
     * Creates a new tariff in the system and associates it with a specific parking.
     * This method validates the provided parking ID and ensures the tariff is correctly linked.
     *
     * @param tariffDto the data transfer object containing tariff details to be created
     * @param parkingId the unique identifier of the parking to associate the tariff with
     * @return the created tariff as a data transfer object
     * @throws ParkingNotFoundException if parking with the specified ID is not found
     * @throws ExternalServiceException if there is an error communicating with external services
     */
    TariffDto create(TariffDto tariffDto, UUID parkingId);

    void updateTariffCurrency(UUID tariffId, String currency);

    /**
     * Updates an existing tariff in the system with the provided information.
     * This method modifies the tariff details based on the given ID and data transfer object.
     *
     * @param tariffId the unique identifier of the tariff to be updated
     * @param tariffDto the data transfer object containing updated tariff details
     * @return the updated tariff as a data transfer object
     * @throws TariffNotFoundException if tariff with the specified ID is not found
     * @throws TariffAlreadyExistsException if a tariff with the same class already exists
     */
    TariffDto update(UUID tariffId, UpdateTariffRequest tariffDto);


    /**
     * Deletes a tariff identified by its unique ID.
     * This method marks the tariff as deleted in the system.
     *
     * @param tariffId the unique identifier of the tariff to be deleted
     * @throws TariffNotFoundException if the tariff with the specified ID is not found
     * @throws pl.edu.zut.app.parking.tariffs.exceptions.TariffIllegalStateException if the tariff is already deleted
     */
    void delete(UUID tariffId);


    /**
     * Retrieves all tariffs associated with a specific parking.
     *
     * @param parkingId the unique identifier of the parking for which tariffs are to be retrieved
     * @param minutes
     * @param currency
     * @return a list of data transfer objects representing the tariffs associated with the given parking
     */
    List<TariffDto> findAllByTime(UUID parkingId, Long minutes, String currency);

    List<TariffDto> findAllByParkingId(UUID parkingId);


    /**
     * Retrieves the tariff identified by the specified unique identifier.
     *
     * @param tariffId the unique identifier of the tariff to be retrieved
     * @param currency
     * @return the tariff as a data transfer object, or null if the tariff is not found
     * @throws TariffNotFoundException if the tariff with the specified ID is not found
     */
    TariffDto find(UUID tariffId, String currency);

    void createBaseTariff(CreatedParkingMessage message);

    void updateParkingCurrency(ChangeParkingCurrencyMessage message);
}
