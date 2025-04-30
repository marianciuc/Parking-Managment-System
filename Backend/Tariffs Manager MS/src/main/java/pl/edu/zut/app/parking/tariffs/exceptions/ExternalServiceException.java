package pl.edu.zut.app.parking.tariffs.exceptions;

public class ExternalServiceException extends TariffServiceException {
    public ExternalServiceException(String errorFetchingParkingInformation) {
        super(errorFetchingParkingInformation);
    }
}
