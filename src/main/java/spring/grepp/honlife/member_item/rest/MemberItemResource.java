package spring.grepp.honlife.member_item.rest;

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
import spring.grepp.honlife.member_item.model.MemberItemDTO;
import spring.grepp.honlife.member_item.service.MemberItemService;


@RestController
@RequestMapping(value = "/api/memberItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberItemResource {

    private final MemberItemService memberItemService;

    public MemberItemResource(final MemberItemService memberItemService) {
        this.memberItemService = memberItemService;
    }

    @GetMapping
    public ResponseEntity<List<MemberItemDTO>> getAllMemberItems() {
        return ResponseEntity.ok(memberItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberItemDTO> getMemberItem(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(memberItemService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMemberItem(
            @RequestBody @Valid final MemberItemDTO memberItemDTO) {
        final Integer createdId = memberItemService.create(memberItemDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateMemberItem(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final MemberItemDTO memberItemDTO) {
        memberItemService.update(id, memberItemDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMemberItem(@PathVariable(name = "id") final Integer id) {
        memberItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
