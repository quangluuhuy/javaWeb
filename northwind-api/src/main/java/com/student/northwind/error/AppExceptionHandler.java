package com.student.northwind.error;

import com.student.northwind.util.component.MessageResourceComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AppExceptionHandler {
    private final MessageResourceComponent messageSource;

    @ExceptionHandler(value = ErrorCodeException.class)
    public ResponseEntity<ErrorResponse> eceHandler(HttpServletRequest request, ErrorCodeException ex) {
        return makeResponse(request, ex.getErrorCode(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> maneHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        return processFieldError(request, ErrorCode.NWD_006_METHOD_ARGS_NOT_VALID, ex);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> maneHandler(HttpServletRequest request, BindException ex) {
        BindingResult result = ex.getBindingResult();
        return getErrorResponseResponseEntity(request, ErrorCode.NWD_004_INVALID_REQUEST, ex, result);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> dtpeHandler(HttpServletRequest request, DateTimeParseException ex) {
        return makeResponse(request, ErrorCode.NWD_003_INVALID_FORMAT, ex);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> npeHandler(HttpServletRequest request, NullPointerException ex) {
        return makeResponse(request, ErrorCode.NWD_008_NULL_POINTER, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> maneHandler(HttpServletRequest request, BadCredentialsException ex) {
        return makeResponse(request, ErrorCode.NWD_101_LOGIN_FAIL, ex);
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ErrorResponse> throwableException(HttpServletRequest request, Throwable ex) {
        return makeResponse(request, ErrorCode.NWD_001_INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponse> processFieldError(HttpServletRequest request,
                                                            ErrorCode errorCode,
                                                            MethodArgumentNotValidException ex) {
        var result = ex.getBindingResult();
        var fieldErrors = result.getFieldErrors();
        var err = new ErrorResponse(errorCode.code(), messageSource.getMessage(errorCode.message()));
        logErrorRequest(request, errorCode.httpStatus(), ex);
        fieldErrors.forEach(
                fieldError ->
                        err.addFieldError(
                                new ErrorField(
                                        fieldError.getObjectName(),
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage())));
        return new ResponseEntity<>(err, errorCode.httpStatus());
    }


    private ResponseEntity<ErrorResponse> makeResponse(HttpServletRequest request, ErrorCode errorCode, ErrorCodeException ex) {
        var err = new ErrorResponse(errorCode.code(), messageSource.getMessage(ex.getMessage(), ex.getParams()));
        logErrorRequest(request, errorCode.httpStatus(), ex);
        return new ResponseEntity<>(err, errorCode.httpStatus());
    }

    private ResponseEntity<ErrorResponse> makeResponse(HttpServletRequest request, ErrorCode errorCode, Throwable ex) {
        var err = new ErrorResponse(errorCode.code(), messageSource.getMessage(errorCode.message()));
        logErrorRequest(request, errorCode.httpStatus(), ex);
        return new ResponseEntity<>(err, errorCode.httpStatus());
    }

    private void logErrorRequest(HttpServletRequest request, HttpStatus httpStatus, Throwable ex) {
        log.error("Request:{}, status:{}, message={}", request.getRequestURI(), httpStatus, ex.getMessage(), ex);
    }

    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpServletRequest request, ErrorCode errorCode, Exception ex, BindingResult result) {
        var fieldErrors = result.getFieldErrors();

        var err = new ErrorResponse(errorCode.code(), messageSource.getMessage(errorCode.message()));
        logErrorRequest(request, errorCode.httpStatus(), ex);
        fieldErrors.forEach(fieldError -> err.addFieldError(new ErrorField(fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getDefaultMessage())));
        return new ResponseEntity<>(err, errorCode.httpStatus());
    }
}
