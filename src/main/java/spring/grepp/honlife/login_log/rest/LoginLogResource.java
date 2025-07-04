package spring.grepp.honlife.login_log.rest;

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
import spring.grepp.honlife.login_log.model.LoginLogDTO;
import spring.grepp.honlife.login_log.service.LoginLogService;


@RestController
@RequestMapping(value = "/api/loginLogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginLogResource {

    private final LoginLogService loginLogService;

    public LoginLogResource(final LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    @GetMapping
    public ResponseEntity<List<LoginLogDTO>> getAllLoginLogs() {
        return ResponseEntity.ok(loginLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginLogDTO> getLoginLog(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(loginLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createLoginLog(
            @RequestBody @Valid final LoginLogDTO loginLogDTO) {
        final Integer createdId = loginLogService.create(loginLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateLoginLog(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final LoginLogDTO loginLogDTO) {
        loginLogService.update(id, loginLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLoginLog(@PathVariable(name = "id") final Integer id) {
        loginLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
