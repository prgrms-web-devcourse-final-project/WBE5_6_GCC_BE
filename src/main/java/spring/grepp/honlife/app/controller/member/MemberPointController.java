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
import spring.grepp.honlife.app.model.member.model.MemberPointDTO;
import spring.grepp.honlife.app.model.member.service.MemberPointService;
import spring.grepp.honlife.infra.util.ReferencedException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/memberPoints", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberPointController {

    private final MemberPointService memberPointService;

    public MemberPointController(final MemberPointService memberPointService) {
        this.memberPointService = memberPointService;
    }

    @GetMapping
    public ResponseEntity<List<MemberPointDTO>> getAllMemberPoints() {
        return ResponseEntity.ok(memberPointService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberPointDTO> getMemberPoint(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberPointService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberPoint(
        @RequestBody @Valid final MemberPointDTO memberPointDTO) {
        final Long createdId = memberPointService.create(memberPointDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberPoint(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final MemberPointDTO memberPointDTO) {
        memberPointService.update(id, memberPointDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberPoint(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = memberPointService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberPointService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
