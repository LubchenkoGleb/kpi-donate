package kpi.donate.rest;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/maecenas")
public class MaecenasController {

    @PostMapping(value = "/confirm")
    private ResponseEntity<String> confirmDonate(@RequestBody JsonNode body) {
        log.info("confirmDonate '{}'", body);
        return ResponseEntity.ok(body.toString());
    }
}
