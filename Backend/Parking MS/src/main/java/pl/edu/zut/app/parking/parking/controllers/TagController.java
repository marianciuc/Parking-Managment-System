package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.dto.common.ParkingTagDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingTagCreateRequest;
import pl.edu.zut.app.parking.parking.services.TagService;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingTagDto> createTag(@RequestBody ParkingTagCreateRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID tagId) {
        tagService.deleteTagById(tagId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingTagDto> updateTag(@PathVariable UUID tagId, @RequestBody ParkingTagDto request) {
        return ResponseEntity.ok(tagService.updateTag(tagId, request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParkingTagDto>> getTags(@RequestParam(name = "tagId", required = false) UUID tagId,
                                                       @RequestParam(name = "name", required = false) String name) {
        log.info("Searching for tag with id: {} and name: {}", tagId, name);
        return ResponseEntity.ok(tagService.findTag(tagId, name));
    }
}
