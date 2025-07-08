package spring.grepp.honlife.app.controller.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.grepp.honlife.app.model.item.dto.ItemDTO;
import spring.grepp.honlife.app.model.item.service.ItemService;

import java.util.List;

@Tag(name = "아이템", description = "아이템 관련 api 입니다.")
@RestController
@RequestMapping(value = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "아이템 정보 전체 조회", description = "상점에 있는 아이템 정보 전체를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "아이템 정보 예시",
                            value = """
                                    {
                                    "status" : 2000,
                                    "message" : "OK",
                                    "data" : [
                                      {
                                        "item_id": 9007199254740991,
                                        "createdAt": "2025-07-08T03:33:58.344Z",
                                        "updatedAt": "2025-07-08T03:33:58.344Z",
                                        "itemKey": "string",
                                        "name": "string",
                                        "price": 1073741824,
                                        "type": "HEAD",
                                        "isActive": true
                                      }
                                    ]
                                    }
                                    """
                    )

            )

    )
    public ResponseEntity<List<ItemDTO>> getAllItems(
            @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(itemService.findAll());
    }

    @Operation(summary = "아이템 구매", description = "포인트 차감 후 아이템을 구매합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(itemService.get(id));
    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createItem(@RequestBody @Valid final ItemDTO itemDTO) {
//        final Long createdId = itemService.create(itemDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> updateItem(@PathVariable(name = "id") final Long id,
//        @RequestBody @Valid final ItemDTO itemDTO) {
//        itemService.update(id, itemDTO);
//        return ResponseEntity.ok(id);
//    }

//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteItem(@PathVariable(name = "id") final Long id) {
//        final ReferencedWarning referencedWarning = itemService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        itemService.delete(id);
//        return ResponseEntity.noContent().build();
//    }

}
