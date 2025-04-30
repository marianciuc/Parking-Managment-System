package pl.edu.zut.app.parking.parking.services;

import pl.edu.zut.app.parking.parking.dto.common.ParkingTagDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingTagCreateRequest;
import pl.edu.zut.app.parking.parking.entities.Tag;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing operations related to parking tags.
 */
public interface TagService {

    /**
     * Creates a new tag with the provided details.
     *
     * @param tag the TagDto object containing information for the new tag, such as name, description, or metadata
     * @return the newly created tag as a TagDto
     */
    ParkingTagDto createTag(ParkingTagCreateRequest tag);

    /**
     * Retrieves a tag by its unique identifier.
     *
     * @param id the unique identifier of the tag to retrieve
     * @return the tag corresponding to the provided identifier as a TagDto
     * @throws pl.edu.zut.app.parking.parking.exceptions.NotFoundException if no tag with the provided identifier exists
     */
    ParkingTagDto findTagById(UUID id);

    /**
     * Deletes a tag identified by its unique identifier.
     *
     * @param id the unique identifier of the tag to be deleted
     * @throws pl.edu.zut.app.parking.parking.exceptions.NotFoundException if no tag with the provided identifier exists
     */
    void deleteTagById(UUID id);

    /**
     * Updates an existing tag with the provided details.
     *
     * @param tagId
     * @param parkingTagDto the TagDto object containing updated information for the tag, such as name, description, or metadata
     * @return the updated tag as a TagDto, including its identifier, name, description, and metadata
     * @throws pl.edu.zut.app.parking.parking.exceptions.NotFoundException if no tag with the provided identifier exists
     */
    ParkingTagDto updateTag(UUID tagId, ParkingTagDto parkingTagDto);

    /**
     * Retrieves a list of tags based on their unique identifiers.
     *
     * @param tags a list of UUIDs representing the unique identifiers of the tags to retrieve
     * @return a list of Tag objects corresponding to the provided identifiers
     */
    List<Tag> getTagsByIds(List<UUID> tags);

    /**
     * Finds and retrieves a list of parking tags based on the specified tag ID and name.
     *
     * @param tagId the unique identifier of the tag (UUID) to search for
     * @param name the name of the tag to search for
     * @return a list of ParkingTagDto objects matching the specified criteria
     */
    List<ParkingTagDto> findTag(UUID tagId, String name);
}
