package spring.grepp.honlife.app.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import spring.grepp.honlife.app.model.member.model.MemberDTO;
import spring.grepp.honlife.app.model.member.service.MemberService;
import spring.grepp.honlife.infra.util.ReferencedException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@Tag(name = "회원", description = "회원관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /**
     * 특정 회원 정보 조회 API
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "특정 회원 정보 조회", description = "특정 회원에 대한 정보를 조회합니다.")
    @ApiResponse(responseCode = "2000", description = "OK",
        content = @Content(schema = @Schema(implementation = MemberDTO.class)))
    public ResponseEntity<MemberDTO> getMember(
        @PathVariable(name = "id")
        @Schema(description = "Path Value", example = "1") final Long id
    ) {
        return ResponseEntity.ok(memberService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMember(@RequestBody @Valid final MemberDTO memberDTO) {
        final Long createdId = memberService.create(memberDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMember(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final MemberDTO memberDTO) {
        memberService.update(id, memberDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
