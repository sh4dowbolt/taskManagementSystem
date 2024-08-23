package com.suraev.TaskManagementSystem.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.jackson.ProblemModule;

@Configuration
public class ApplicationConfiguration {
    @Bean
    ObjectMapper objectMapperProblem() {

        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new ProblemModule()).registerModule(new JsonNullableModule());
    }
    // отключаем стак трейс для корректного вывода exceptionHandlers
    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule().withStackTraces(true);
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder.build();
    }

}

