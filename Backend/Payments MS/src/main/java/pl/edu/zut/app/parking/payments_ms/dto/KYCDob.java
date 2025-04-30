package pl.edu.zut.app.parking.payments_ms.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * A record representing the date of birth for a KYC process.
 *
 * This record encapsulates the day, month, and year components of a date,
 * each with its respective validation constraints.
 *
 * This record is utilized as part of the KYC data structure to validate
 * and represent a user's birth date.
 *
 * @param day   Day of the month (1-31).
 * @param month Month of the year (1-12).
 * @param year  Year of birth (1900-2100).
 */
public record KYCDob(
    @Min(1) @Max(31) long day, @Min(1) @Max(12) long month, @Min(1900) @Max(2100) long year) {}
