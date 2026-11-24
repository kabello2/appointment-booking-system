package com.javaguru.appointmentbooking.utils.errorhandling;

import com.javaguru.appointmentbooking.dto.response.ErrorResponse;
import com.javaguru.appointmentbooking.utils.constants.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String field =
                            ((FieldError) error).getField();

                    String message =
                            error.getDefaultMessage();

                    errors.put(field, message);
                });

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ErrorCodes.VALIDATION_ERROR,
                        "Validation failed",
                        request.getRequestURI(),
                        errors
                );

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ErrorCodes.VALIDATION_ERROR,
                        ex.getMessage(),
                        request.getRequestURI(),
                        null
                );

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ErrorCodes.USER_ALREADY_EXISTS,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCodes.USER_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBranchNotFound(
            BranchNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCodes.BRANCH_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ServiceOfferingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleServiceNotFound(
            ServiceOfferingNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCodes.SERVICE_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFound(
            AppointmentNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCodes.APPOINTMENT_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(DuplicateAppointmentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateAppointment(
            DuplicateAppointmentException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ErrorCodes.DUPLICATE_BOOKING,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedAccount(
            AccountLockedException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.LOCKED,
                ErrorCodes.ACCOUNT_LOCKED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleViolationException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BUSINESS_RULE_VIOLATION,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.FORBIDDEN,
                ErrorCodes.ACCESS_DENIED,
                "You are not authorized to perform this action.",
                request
        );
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(
            RateLimitExceededException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.TOO_MANY_REQUESTS,
                ErrorCodes.RATE_LIMIT_EXCEEDED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.error("Database constraint violation", ex);

        return buildResponse(
                HttpStatus.CONFLICT,
                ErrorCodes.DATABASE_ERROR,
                "Database integrity constraint violated.",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodes.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.",
                request
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.AUTHENTICATION_FAILED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(
            InvalidTokenException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.INVALID_TOKEN,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpiredToken(
            TokenExpiredException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.TOKEN_EXPIRED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledAccount(
            AccountDisabledException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.ACCOUNT_DISABLED,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ErrorCodes.RESOURCE_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String errorCode,
            String message,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        errorCode,
                        message,
                        request.getRequestURI(),
                        null
                );

        return ResponseEntity.status(status)
                .body(response);
    }
}
