package spring.grepp.honlife.app.controller.member;

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
import spring.grepp.honlife.app.model.member.model.MemberImageDTO;
import spring.grepp.honlife.app.model.member.service.MemberImageService;
import spring.grepp.honlife.infra.util.ReferencedException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/memberImages", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberImageController {

    private final MemberImageService memberImageService;

    public MemberImageController(final MemberImageService memberImageService) {
        this.memberImageService = memberImageService;
    }

    @GetMapping
    public ResponseEntity<List<MemberImageDTO>> getAllMemberImages() {
        return ResponseEntity.ok(memberImageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberImageDTO> getMemberImage(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberImageService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberImage(
            @RequestBody @Valid final MemberImageDTO memberImageDTO) {
        final Long createdId = memberImageService.create(memberImageDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberImage(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberImageDTO memberImageDTO) {
        memberImageService.update(id, memberImageDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberImage(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = memberImageService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberImageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
