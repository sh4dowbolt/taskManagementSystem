package com.suraev.TaskManagementSystem.controller;

import com.suraev.TaskManagementSystem.domain.entity.Health;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Tag("HealthController")
class HealthResourceTest {
    @InjectMocks
    private HealthResource healthResource;
    @Test
    void checkHealth() {
        ResponseEntity<Health> actualResult = healthResource.getHealth();
        assertThat(actualResult.getStatusCode().is2xxSuccessful());
    }

}