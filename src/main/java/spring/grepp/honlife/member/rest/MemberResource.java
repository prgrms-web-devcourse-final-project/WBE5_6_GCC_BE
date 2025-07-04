package spring.grepp.honlife.member.rest;

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
import spring.grepp.honlife.member.model.MemberDTO;
import spring.grepp.honlife.member.service.MemberService;
import spring.grepp.honlife.util.ReferencedException;
import spring.grepp.honlife.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberResource {

    private final MemberService memberService;

    public MemberResource(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(memberService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMember(@RequestBody @Valid final MemberDTO memberDTO) {
        final Integer createdId = memberService.create(memberDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMember(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final MemberDTO memberDTO) {
        memberService.update(id, memberDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
