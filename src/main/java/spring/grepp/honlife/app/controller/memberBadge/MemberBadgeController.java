package spring.grepp.honlife.app.controller.memberBadge;

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
import spring.grepp.honlife.app.model.memberBadge.dto.MemberBadgeDTO;
import spring.grepp.honlife.app.model.memberBadge.service.MemberBadgeService;


@RestController
@RequestMapping(value = "/api/memberBadges", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;

    public MemberBadgeController(final MemberBadgeService memberBadgeService) {
        this.memberBadgeService = memberBadgeService;
    }

    @GetMapping
    public ResponseEntity<List<MemberBadgeDTO>> getAllMemberBadges() {
        return ResponseEntity.ok(memberBadgeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberBadgeDTO> getMemberBadge(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(memberBadgeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMemberBadge(
            @RequestBody @Valid final MemberBadgeDTO memberBadgeDTO) {
        final Integer createdId = memberBadgeService.create(memberBadgeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMemberBadge(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final MemberBadgeDTO memberBadgeDTO) {
        memberBadgeService.update(id, memberBadgeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberBadge(@PathVariable(name = "id") final Integer id) {
        memberBadgeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
