package com.suraev.TaskManagementSystem.controller;


import com.suraev.TaskManagementSystem.domain.entity.Health;
import com.suraev.TaskManagementSystem.domain.entity.enums.HealthStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name ="Check health endpoint", description = "Проверка работоспособности приложения")
public class HealthResource {

    @Operation(summary = "Проверка статуса")
    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<Health> getHealth() {
        final var health = new Health();
        health.setStatus(HealthStatus.UP);
        return ResponseEntity.ok().body(health);
    }
}
