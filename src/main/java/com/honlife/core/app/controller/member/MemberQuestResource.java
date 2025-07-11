package com.honlife.core.app.controller.member;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import com.honlife.core.app.model.member.model.MemberQuestDTO;
import com.honlife.core.app.model.member.service.MemberQuestService;



@Tag(name="회원 보유 퀘스트", description = "현재 로그인한 회원이 보유하고 있는 퀘스트 관련 API 입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/members/quests", produces = MediaType.APPLICATION_JSON_VALUE)

public class MemberQuestResource {

    private final MemberQuestService memberQuestService;

    public MemberQuestResource(final MemberQuestService memberQuestService) {
        this.memberQuestService = memberQuestService;
    }

    
    @GetMapping
    public ResponseEntity<List<MemberQuestDTO>> getAllMemberQuests() {
        return ResponseEntity.ok(memberQuestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQuestDTO> getMemberQuest(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(memberQuestService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMemberQuest(
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        final Long createdId = memberQuestService.create(memberQuestDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMemberQuest(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberQuestDTO memberQuestDTO) {
        memberQuestService.update(id, memberQuestDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberQuest(@PathVariable(name = "id") final Long id) {
        memberQuestService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
