package pl.edu.zut.app.parking.parking.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.entities.OpeningHours;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.*;
import pl.edu.zut.app.parking.parking.repositories.OpeningHoursRepository;
import pl.edu.zut.app.parking.parking.services.OpeningHoursService;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpeningHourServiceImpl implements OpeningHoursService {

    private static final LocalTime OPEN_24H_TIME = LocalTime.MIN;
    private static final LocalTime CLOSE_24H_TIME = LocalTime.of(23, 59);

    private final OpeningHoursRepository openingHoursRepository;

    @Transactional
    public void createOpeningHours(Parking parking, List<OpeningHoursDto> openingHoursDtoList) {
        log.debug("Creating specific opening hours for parking: {}", parking.getId());

        if (!parking.getOpeningHours().isEmpty()) {
            log.error("Opening hours already exist for parking: {}", parking.getId());
            throw new OpeningHoursAlreadyExistException("Opening hours already exist for parking with id: " + parking.getId());
        }

        validateOpeningHoursDtoList(openingHoursDtoList);
        List<OpeningHours> openingHours = openingHoursDtoList.stream()
                .<OpeningHours>map(openingHoursDto -> OpeningHours.builder()
                        .closingTime(openingHoursDto.closingTime())
                        .openingTime(openingHoursDto.openingTime())
                        .isClosed(openingHoursDto.isClosed())
                        .dayOfWeek(openingHoursDto.dayOfWeek())
                        .parking(parking)
                        .build())
                .toList();
        parking.setOpeningHours(openingHours);
    }

    @Override
    @Transactional
    public void updateOpeningHours(UUID parkingId, List<OpeningHoursDto> openingHoursDtoList) {
        log.info("Updating opening hours for parking id: {}", parkingId);
        validateOpeningHoursDtoList(openingHoursDtoList);

        List<OpeningHours> openingHours = openingHoursRepository.findAllByParkingId(parkingId);

        if (openingHours.isEmpty()) {
            log.error("No opening hours found for parking id: {}", parkingId);
            throw new MissingOpeningHoursException("No opening hours found for parking with id: " + parkingId);
        }

        Map<OpeningHours.DayOfWeek, OpeningHoursDto> dtoMap = openingHoursDtoList.stream()
                .collect(Collectors.toMap(OpeningHoursDto::dayOfWeek, dto -> dto, (existing, replacement) -> replacement));

        openingHours.forEach(openingHour -> {
            log.debug("Updating opening hour for day: {} of parking {}", openingHour.getDayOfWeek(), parkingId);

            OpeningHoursDto openingHoursDto = dtoMap.get(openingHour.getDayOfWeek());
            if (openingHoursDto == null) {
                throw new MissingDayOpeningHoursException(openingHour.getDayOfWeek().toString());
            }
            openingHour.setOpeningTime(openingHoursDto.openingTime());
            openingHour.setClosingTime(openingHoursDto.closingTime());
            openingHour.setClosed(openingHoursDto.isClosed());
        });

        log.info("Successfully updated opening hours for parking {}", parkingId);
    }


    @Override
    @Transactional
    public void deleteOpeningHours(Parking parking) {
        log.info("Deleting opening hours for parking: {}", parking.getId());

        List<UUID> openingHourIds = parking.getOpeningHours().stream()
                .map(OpeningHours::getId)
                .collect(Collectors.toList());

        openingHoursRepository.deleteAllByIdInBatch(openingHourIds);

        parking.setOpeningHours(List.of());

        log.info("Successfully deleted opening hours for parking {}", parking.getId());
    }

    @Override
    public List<OpeningHoursDto> getOpeningHours(UUID parkingId) {
        log.info("Fetching opening hours for parking id: {}", parkingId);

        List<OpeningHours> openingHours = openingHoursRepository.findAllByParkingId(parkingId);
        if (openingHours.isEmpty() || openingHours.getFirst().getParking().is24h()) {
            log.error("No opening hours found or parking {} is in 24/7 mode.", parkingId);
            throw new InvalidOpeningHoursException("No opening hours found for parking with id: " + parkingId);
        }

        log.info("Successfully fetched opening hours for parking {}", parkingId);
        return openingHours.stream().map(OpeningHoursDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void create24HourOpeningHours(Parking parking) {
        log.info("Creating 24/7 opening hours for parking: {}", parking.getId());

        if (parking.getOpeningHours() != null && !parking.getOpeningHours().isEmpty()) {
            log.error("Opening hours already exist for parking: {}", parking.getId());
            throw new OpeningHoursAlreadyExistException("Opening hours already exist for parking with id: " + parking.getId());
        }

        List<OpeningHours> openingHours = EnumSet.allOf(OpeningHours.DayOfWeek.class).stream()
                .map(dayOfWeek -> OpeningHours.builder()
                        .dayOfWeek(dayOfWeek)
                        .openingTime(OPEN_24H_TIME)
                        .closingTime(CLOSE_24H_TIME)
                        .parking(parking)
                        .build()).collect(Collectors.toList());

        parking.setOpeningHours(openingHours);

        log.info("Successfully created 24/7 opening hours for parking {}", parking.getId());
    }

    @Override
    @Transactional
    public void updateOpeningHours(Parking parking, boolean is24, List<OpeningHoursDto> hours) {
        if (is24) {
            if (!parking.is24h()) {
                parking.set24h(true);
                deleteOpeningHours(parking);
                create24HourOpeningHours(parking);
            }
        } else {
            if (parking.is24h()) {
                parking.set24h(false);
            }
            deleteOpeningHours(parking);

            if (hours != null && !hours.isEmpty()) {
                createOpeningHours(parking, hours);
            } else {
                throw new IllegalArgumentException("Working hours must be provided if not 24/7.");
            }
        }
    }

    /**
     * Validates a list of {@code OpeningHoursDto} objects to ensure that the opening and
     * closing times are logical and do not overlap for each day of the week.
     *
     * @param openingHoursDtoList the list of {@code OpeningHoursDto} instances to validate
     *                            containing opening and closing time details for different
     *                            days of the week
     * @throws InvalidOpeningHoursException if:
     *         - The opening time is after the closing time for any entry.
     *         - Overlapping time intervals are found within the same day.
     */
    private void validateOpeningHoursDtoList(List<OpeningHoursDto> openingHoursDtoList) {
        for (OpeningHoursDto dto : openingHoursDtoList) {
            if (dto.openingTime().isAfter(dto.closingTime())) {
                throw new InvalidOpeningHoursException(
                        "Opening time cannot be after closing time for day: " + dto.dayOfWeek());
            }
        }

        Map<OpeningHours.DayOfWeek, List<OpeningHoursDto>> groupedByDay = openingHoursDtoList.stream()
                .collect(Collectors.groupingBy(OpeningHoursDto::dayOfWeek));

        for (Map.Entry<OpeningHours.DayOfWeek, List<OpeningHoursDto>> entry : groupedByDay.entrySet()) {
            List<OpeningHoursDto> hoursForDay = entry.getValue();
            hoursForDay.sort(Comparator.comparing(OpeningHoursDto::openingTime));

            for (int i = 0; i < hoursForDay.size() - 1; i++) {
                if (hoursForDay.get(i).closingTime().isAfter(hoursForDay.get(i + 1).openingTime())) {
                    throw new InvalidOpeningHoursException(
                            "Overlapping hours detected for day: " + entry.getKey());
                }
            }
        }
    }
}
