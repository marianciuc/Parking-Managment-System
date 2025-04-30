package pl.edu.zut.app.parking.admin_ms.dto;

import java.time.LocalDate;

public record AdministratorUpdateRequest(
        String firstname,
        String lastname,
        LocalDate dateOfBirth,
        String posititon,
        String profileImageUrl,
        String phoneNumber
) {}
