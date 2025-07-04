package spring.grepp.honlife.app.controller.memberQuest;

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
import spring.grepp.honlife.app.model.memberQuest.dto.MemberQuestDTO;
import spring.grepp.honlife.app.model.memberQuest.service.MemberQuestService;


@RestController
@RequestMapping(value = "/api/memberQuests", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberQuestController {

    private final MemberQuestService memberQuestService;

    public MemberQuestController(final MemberQuestService memberQuestService) {
        this.memberQuestService = memberQuestService;
    }

    @GetMapping
    public ResponseEntity<List<MemberQuestDTO>> getAllMemberQuests() {
        return ResponseEntity.ok(memberQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQuestDTO> getMemberQuest(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(memberQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMemberQuest(
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        final Integer createdId = memberQuestService.create(memberQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMemberQuest(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        memberQuestService.update(id, memberQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberQuest(@PathVariable(name = "id") final Integer id) {
        memberQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
