package com.invenia.portalservice.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("portal-service/v1/config")
public class ConfigController {

  private final ConfigRepository configRepository;

  @GetMapping("/{id}")
  public ResponseEntity<Config> get(@PathVariable String id) {
    return configRepository
        .findById(id)
        .map(config -> ResponseEntity.ok().body(config))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/")
  public ResponseEntity<Config> put(@RequestBody Config config) {
    return ResponseEntity.ok().body(configRepository.save(config));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
    try {
      configRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
