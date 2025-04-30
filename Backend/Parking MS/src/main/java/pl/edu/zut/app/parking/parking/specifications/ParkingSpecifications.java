package pl.edu.zut.app.parking.parking.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingDetailsRequest;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;

import java.util.List;
import java.util.UUID;

/**
 * Utility class providing static factory methods to create {@link Specification} instances
 * for querying the {@link Parking} entity. The specifications are designed to support
 * complex and dynamic query-building, enabling filtering based on various attributes.
 */
public class ParkingSpecifications {

    private ParkingSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Constructs a Specification for the Parking entity to filter by name containing a specific substring.
     * The filtering is case-insensitive and matches names that include the provided substring.
     *
     * @param name the substring to match within the Parking name. If null or blank, no filtering will be applied.
     * @return a Specification for filtering Parking entities based on their name, or null if no filtering is required.
     */
    public static Specification<Parking> whereNameContains(String name) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by a specific UUID identifier.
     * The resulting criteria will match Parking entities whose "id" attribute equals the provided UUID.
     *
     * @param id the UUID to compare with the "id" attribute of Parking entities. If null, no filter will be applied.
     * @return a Specification for filtering Parking entities based on their UUID identifier, or null if the provided UUID is null.
     */
    public static Specification<Parking> whereIdEquals(UUID id) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by a specific country code.
     * The filtering is applied based on the "countryCode" attribute in the associated Address entity.
     *
     * @param country the country code to filter by. If null or blank, no filtering will be applied.
     * @return a Specification for filtering Parking entities by the given country code, or null if the country code is null or blank.
     */
    public static Specification<Parking> whereCountryCodeEquals(String country) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (country == null || country.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("address").get("countryCode"), country);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by city name.
     * The filtering is based on the "city" attribute in the associated Address entity.
     *
     * @param city the name of the city to filter by. If null or blank, no filtering will be applied.
     * @return a Specification for filtering Parking entities by the given city name, or null if the city name is null or blank.
     */
    public static Specification<Parking> hasCity(String city) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (city == null || city.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("address").get("city"), city);
        };
    }


    /**
     * Constructs a Specification for the Parking entity to filter by tag IDs.
     * The filtering is based on the "tags" relationship, specifically matching the "id"
     * attribute of associated Tag entities with the provided list of tag IDs.
     *
     * @param tags the list of UUIDs representing the tag IDs to filter by. If null or empty, no filtering will be applied.
     * @return a Specification for filtering Parking entities based on their associated tag IDs,
     *         or null if the provided list of tags is null or empty.
     */
    public static Specification<Parking> whereTagIdIn(List<UUID> tags) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            Join<Object, Object> tagJoin = root.join("tags");
            return tagJoin.get("id").in(tags);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by the "isClosed" status.
     * The filtering is determined based on whether the provided {@code isOpen} parameter
     * is true or false. If {@code isOpen} is true, it matches entities where "isClosed"
     * is false; if {@code isOpen} is false, it matches entities where "isClosed" is true.
     * No filtering is applied if {@code isOpen} is null.
     *
     * @param isOpen a Boolean indicating the desired "isClosed" status.
     * @return a Specification for filtering Parking entities based on their "isClosed"
     *         status, or null if {@code isOpen} is null.
     */
    public static Specification<Parking> whereIsClosedEquals(Boolean isOpen) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (isOpen == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("isClosed"), !isOpen);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by the "is24h" attribute.
     * The filtering is applied based on whether the provided {@code is24h} parameter matches
     * the "is24h" status of Parking entities. If {@code is24h} is null, no filtering will be applied.
     *
     * @param is24h a Boolean indicating the desired "is24h" status of Parking entities.
     *              If true, it matches entities with "is24h" set to true; if false,
     *              it matches entities with "is24h" set to false. If null, no filtering is applied.
     * @return a Specification for filtering Parking entities based on their "is24h" status,
     *         or null if {@code is24h} is null.
     */
    public static Specification<Parking> whereIs24hEquals(Boolean is24h) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (is24h == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("is24h"), is24h);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by the specified record status.
     * The filtering is applied based on the "recordStatus" attribute of the Parking entity.
     *
     * @param recordStatus the {@link AbstractBaseEntity.RecordStatus} to filter by.
     *                     If null, no filtering will be applied.
     * @return a Specification for filtering Parking entities by their record status,
     *         or null if the provided record status is null.
     */
    public static Specification<Parking> whereRecordStatus(AbstractBaseEntity.RecordStatus recordStatus) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (recordStatus == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("recordStatus"), recordStatus);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by access type.
     * The filtering is applied to the "accessType" attribute of Parking entities.
     *
     * @param accessType the {@link AccessType} to filter by. If null, no filtering will be applied.
     * @return a Specification for filtering Parking entities based on their access type,
     *         or null if the provided access type is null.
     */
    public static Specification<Parking> whereAccessType(AccessType accessType) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (accessType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("accessType"), accessType);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by its parking status.
     * The filtering is based on the "parkingStatus" attribute of the Parking entity.
     *
     * @param parkingStatus the parking status to filter by. If null, no filtering will be applied.
     * @return a Specification for filtering Parking entities by their parking status, or null if the provided parking status is null.
     */
    public static Specification<Parking> whereParkingStatus(ParkingStatus parkingStatus) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (parkingStatus == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("parkingStatus"), parkingStatus);
        };
    }

    /**
     * Constructs a Specification for the Parking entity to filter by geographical proximity
     * within a specified radius from a given latitude and longitude.
     * The Haversine formula is used to calculate the distance in meters between the provided
     * user coordinates and the parking location.
     *
     * @param userLatitude the latitude of the user's location. If NaN, no filtering will be applied.
     * @param userLongitude the longitude of the user's location. If NaN, no filtering will be applied.
     * @param radiusInMeters the radius (in meters) within which to filter Parking entities. If NaN, no filtering will be applied.
     * @return a Specification for filtering Parking entities within the specified radius, or null if any of the parameters are invalid.
     */
    public static Specification<Parking> withinRadius(Double userLatitude, Double userLongitude, Double radiusInMeters){
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (userLatitude == null || userLongitude == null || radiusInMeters == null) {
                return null;
            }
            double earthRadius = 6371000;

            // Haversine formula:
            // distance = 6371000 * acos(cos(radians(lat1)) * cos(radians(lat2)) * cos(radians(lon2 - lon1)) + sin(radians(lat1)) * sin(radians(lat2)))
            var haversine = cb.prod(
                    cb.literal(earthRadius),
                    cb.function("acos", Double.class,
                            cb.sum(
                                    cb.prod(
                                        cb.prod(
                                                cb.function("cos", Double.class, cb.function("radians", Double.class, cb.literal(userLatitude))),
                                                cb.function("cos", Double.class, cb.function("radians", Double.class, root.get("latitude")))
                                        ),
                                        cb.function("cos", Double.class,
                                                cb.diff(cb.function("radians", Double.class, root.get("longitude")),
                                                        cb.function("radians", Double.class, cb.literal(userLongitude)))
                                        )
                                    ),
                                    cb.prod(
                                            cb.function("sin", Double.class, cb.function("radians", Double.class, cb.literal(userLatitude))),
                                            cb.function("sin", Double.class, cb.function("radians", Double.class, root.get("latitude")))
                                    )
                            )
                    )
            );

            return cb.lessThanOrEqualTo(haversine, radiusInMeters);
        };
    }


    /**
     * Constructs a combined specification for filtering Parking entities based on
     * the parameters provided in the {@link ParkingDetailsRequest}.
     * The resulting Specification applies multiple filters, such as access type,
     * geographical radius, status, tags, and other attributes, based on the values
     * present in the request object.
     *
     * @param request the {@link ParkingDetailsRequest} containing the filtering criteria
     *                for constructing the Specification. If any field in the request
     *                is null or invalid, the corresponding filter will not be applied.
     * @return a {@link Specification} for filtering Parking entities based on the
     *         provided ParkingDetailsRequest, or null if no criteria are specified.
     */
    public static Specification<Parking> fromDetailsRequest(ParkingDetailsRequest request) {
        return Specification.where(whereAccessType(request.accessType()))
                .and(withinRadius(request.latitude(), request.longitude(), request.radiusInMeters()))
                .and(whereParkingStatus(request.parkingStatus()))
                .and(whereIs24hEquals(request.is24h()))
                .and(whereTagIdIn(request.tags()))
                .and(whereCountryCodeEquals(request.countryCode()))
                .and(hasCity(request.city()))
                .and(whereNameContains(request.name()))
                .and(whereRecordStatus(request.recordStatus()))
                .and(whereIdEquals(request.id()))
                .and(whereIsClosedEquals(request.isOpen()));
    }
}
