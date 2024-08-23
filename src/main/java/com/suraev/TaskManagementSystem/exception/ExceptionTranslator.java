package com.suraev.TaskManagementSystem.exception;

import com.suraev.TaskManagementSystem.util.HeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.AdviceTrait;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.routing.MissingServletRequestParameterAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice()
public class ExceptionTranslator implements ProblemHandling, AdviceTrait, MissingServletRequestParameterAdviceTrait {
    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(PATH_KEY, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                    .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                    .withCause(((DefaultProblem) problem).getCause())
                    .withDetail(problem.getDetail())
                    .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }


     @Override
        public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {

            BindingResult result = ex.getBindingResult();
            List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                    .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode()))
                    .collect(Collectors.toList());

            Problem problem = Problem.builder()
                    .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
                    .withTitle("Not valid input data. Check the body of request")
                    .withStatus(defaultConstraintViolationStatus())
                    .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
                    .with(FIELD_ERRORS_KEY, fieldErrors)
                    .build();
            return create(ex, problem, request);
        }
    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert( false, ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoResourceFoundException(NoResourceFoundException ex, NativeWebRequest request) {
        return create(Status.BAD_REQUEST,ex, request, HeaderUtil.createFailureAlert( false, ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleAuthenticationServiceException(AuthenticationServiceException ex, NativeWebRequest request) {
        return create(Status.UNAUTHORIZED,ex, request, HeaderUtil.createFailureAlert( false, ex.getMessage()));
    }

}
