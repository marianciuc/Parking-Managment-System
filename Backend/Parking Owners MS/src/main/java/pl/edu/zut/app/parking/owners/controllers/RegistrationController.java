package pl.edu.zut.app.parking.owners.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.requests.PersonalDataRegistrationRequest;
import pl.edu.zut.app.parking.owners.services.ParkingOwnerRegistrationService;

@RestController
@RequestMapping("/api/v1/owners/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final ParkingOwnerRegistrationService registrationService;

    @PostMapping("/step/1")
    public ResponseEntity<Void> registerStep1(@Validated @RequestBody PersonalDataRegistrationRequest request){
        registrationService.fillOwnerData(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step/2")
    public ResponseEntity<Void> registerStep2(@Validated @RequestBody AddressDto request){
        registrationService.fillOwnerAddress(request);
        return ResponseEntity.ok().build();
    }
}
