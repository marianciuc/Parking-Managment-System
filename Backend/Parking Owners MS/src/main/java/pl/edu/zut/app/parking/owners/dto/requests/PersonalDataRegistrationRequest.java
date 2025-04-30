package pl.edu.zut.app.parking.owners.dto.requests;

public record PersonalDataRegistrationRequest (
        String firstName,
        String middleName,
        String lastName,
        String NIP,
        String phoneNumber,
        String phoneNumberCode
){
}
