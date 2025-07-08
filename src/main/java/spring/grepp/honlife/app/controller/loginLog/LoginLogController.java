package spring.grepp.honlife.app.controller.loginLog;

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
import spring.grepp.honlife.app.model.loginLog.dto.LoginLogDTO;
import spring.grepp.honlife.app.model.loginLog.service.LoginLogService;


@RestController
@RequestMapping(value = "/api/v1/loginLogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginLogController {

    private final LoginLogService loginLogService;

    public LoginLogController(final LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping
    public ResponseEntity<List<LoginLogDTO>> getAllLoginLogs() {
        return ResponseEntity.ok(loginLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginLogDTO> getLoginLog(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(loginLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLoginLog(@RequestBody @Valid final LoginLogDTO loginLogDTO) {
        final Long createdId = loginLogService.create(loginLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLoginLog(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final LoginLogDTO loginLogDTO) {
        loginLogService.update(id, loginLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLoginLog(@PathVariable(name = "id") final Long id) {
        loginLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
