package com.suraev.TaskManagementSystem.configuration.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.io.IOException;
@Component
@AllArgsConstructor

    public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private  final ObjectMapper objectMapper;


    @Override
        public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            try {
                filterChain.doFilter(request, response);
            } catch (JwtException e) {

                ThrowableProblem build =
                        Problem.builder().withTitle("UNAUTHORIZED")
                                .withStatus(Status.UNAUTHORIZED).withDetail("the token is expired and not valid anymore").build();

                response.getWriter().write(convertObjectToJson(build));
            }
        }
        private String convertObjectToJson(Object object) throws JsonProcessingException {
            if (object == null) {
                return null;
            }
            return objectMapper.writeValueAsString(object);
        }

    }

