package spring.grepp.honlife.app.controller.category;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.grepp.honlife.app.model.category.dto.InterestCategoryDTO;
import spring.grepp.honlife.app.model.category.service.InterestCategoryService;


@RestController
@RequestMapping(value = "/api/interestCategories", produces = MediaType.APPLICATION_JSON_VALUE)
public class InterestCategoryController {

    private final InterestCategoryService interestCategoryService;

    public InterestCategoryController(final InterestCategoryService interestCategoryService) {
        this.interestCategoryService = interestCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<InterestCategoryDTO>> getAllInterestCategories() {
        return ResponseEntity.ok(interestCategoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterestCategoryDTO> getInterestCategory(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(interestCategoryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createInterestCategory(
            @RequestBody @Valid final InterestCategoryDTO interestCategoryDTO) {
        final Long createdId = interestCategoryService.create(interestCategoryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateInterestCategory(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final InterestCategoryDTO interestCategoryDTO) {
        interestCategoryService.update(id, interestCategoryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteInterestCategory(@PathVariable(name = "id") final Long id) {
        interestCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
