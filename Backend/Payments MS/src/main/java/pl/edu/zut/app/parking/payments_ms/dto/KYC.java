package pl.edu.zut.app.parking.payments_ms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents Know Your Customer (KYC) information necessary for identity verification.
 *
 * <p>This record encapsulates personal and address details that are commonly used to validate a
 * customer's identity as part of a KYC process.
 *
 * @param firstname First name of the customer. Must not be blank and is limited to 50 characters.
 * @param lastname Last name of the customer. Must not be blank and is limited to 50 characters.
 * @param email Email address of the customer. Must follow a valid email format, should not be
 *     blank, and has a maximum length of 100 characters.
 * @param nationalIdNumber National ID number of the customer. Must not be blank and must be
 *     numeric, validated via a regular expression.
 * @param address Address of the customer, represented by a KYCAddress record, which includes
 *     fields such as city, country code, line1, postal code, and state.
 * @param dob Date of birth of the customer, represented by a KYCDob record that includes the day,
 *     month, and year.
 *
 * @see KYCDob
 * @see KYCAddress
 */
public record KYC(
    @NotBlank @Size(max = 50) String firstname,
    @NotBlank @Size(max = 50) String lastname,
    @NotBlank @Email @Size(max = 100) String email,
    @NotBlank @Pattern(regexp = "\\d+", message = "National ID must be numeric")
        String nationalIdNumber,
    KYCAddress address,
    KYCDob dob) {}
