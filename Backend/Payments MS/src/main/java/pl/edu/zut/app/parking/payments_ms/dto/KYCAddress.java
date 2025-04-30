package pl.edu.zut.app.parking.payments_ms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents the address details for a KYC (Know Your Customer) process.
 *
 * <p>This record is used to encapsulate the customer's address information, including the city,
 * country code, street address, postal code, and state. Each field has specific validation
 * constraints to ensure proper data formatting and completeness.
 *
 * @param city The name of the city, must not be blank and limited to 50 characters.
 * @param countryCode A 2-letter uppercase country code adhering to the ISO 3166-1 alpha-2 format.
 *     It must match a regular expression `[A-Z]{2}`.
 * @param line1 The first line of the street address. Must not be blank and has a maximum length of
 *     100 characters.
 * @param postalCode The postal or ZIP code of the address. Must not be blank.
 * @param state The state, province, or region. Has a maximum length of 50 characters and may be
 *     optional.
 */
public record KYCAddress(
    @NotBlank @Size(max = 50) String city,
    @NotBlank
        @Size(min = 2, max = 2)
        @Pattern(regexp = "[A-Z]{2}", message = "Country code should be two uppercase letters")
        String countryCode,
    @NotBlank @Size(max = 100) String line1,
    @NotBlank String postalCode,
    @Size(max = 50) String state) {}
