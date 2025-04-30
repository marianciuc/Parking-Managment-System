package pl.edu.zut.app.parking.parking.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.parking.dto.common.ParkingTagDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingTagCreateRequest;
import pl.edu.zut.app.parking.parking.entities.Tag;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;
import pl.edu.zut.app.parking.parking.repositories.TagRepository;
import pl.edu.zut.app.parking.parking.services.TagService;
import pl.edu.zut.app.parking.parking.specifications.TagSpecifications;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public ParkingTagDto createTag(ParkingTagCreateRequest tag) {
        if (tagRepository.existsByName(tag.name())) {
            throw new IllegalArgumentException("Tag with this name already exists");
        }

        Tag newTag = Tag.builder()
                .name(tag.name())
                .description(tag.description())
                .build();
        return ParkingTagDto.fromEntity(tagRepository.save(newTag));
    }

    @Override
    public ParkingTagDto findTagById(UUID id) {
        return ParkingTagDto.fromEntity(tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag not found")));
    }

    @Override
    public void deleteTagById(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new NotFoundException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    @Override
    public ParkingTagDto updateTag(UUID tagId, ParkingTagDto parkingTagDto) {
        Tag tag = tagRepository.findById(parkingTagDto.id()).orElseThrow(() -> new NotFoundException("Tag not found"));

        tag.setName(parkingTagDto.name());
        tag.setDescription(parkingTagDto.description());

        return ParkingTagDto.fromEntity(tagRepository.save(tag));
    }

    @Override
    public List<Tag> getTagsByIds(List<UUID> tags) {
        return tagRepository.findAllById(tags);
    }

    @Override
    public List<ParkingTagDto> findTag(UUID tagId, String name) {
        Specification<Tag> spec = Specification.where(TagSpecifications.whereIdEquals(tagId))
                .and(TagSpecifications.whereNameContains(name));
        return tagRepository.findAll(spec).stream().map(ParkingTagDto::fromEntity).toList();
    }

}
