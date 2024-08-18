package com.application.taskmanagementsystem.exception;

import com.application.taskmanagementsystem.model.dto.ResponseError;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Обработчик исключения контроллеров. */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
  @ExceptionHandler({InsufficientAuthenticationException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseError handleInsufficientException(Exception ex, WebRequest request) {
    return new ResponseError(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler({HttpClientErrorException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleIllegalArgumentException(
      HttpClientErrorException ex, WebRequest request) {
    log.error("Error 400: Bad Request");
    return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler({
    NoHandlerFoundException.class,
    UserNotFoundException.class,
          TaskNotFoundException.class,
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseError handleNoResourceFoundException(RuntimeException ex, WebRequest request) {
    log.error("Error 404: Not Found");
    return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ResponseError handleMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    log.error("Error 405: Method not supported");
    return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
  }

  @ExceptionHandler({ExpiredJwtException.class, AccessDeniedException.class,UnauthorizedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseError handleUnauthorizedException(RuntimeException ex, WebRequest request) {
    log.error("Error 403: Forbidden");
    return new ResponseError(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseError handleAllExceptions(RuntimeException ex, WebRequest request) {
    log.error("Error 500: Internal Server Error");
    return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleInvalidArgument(MethodArgumentNotValidException ex){
    StringBuilder errorMessageBuilder = new StringBuilder();
    ex.getBindingResult().getFieldErrors().forEach(error ->
            errorMessageBuilder.append(error.getDefaultMessage())
                    .append("; ")
    );
    String errorMessage = errorMessageBuilder.toString().trim();
    log.error("Error 400: Bad Request");
    return new ResponseError(HttpStatus.BAD_REQUEST, errorMessage);
  }
  @ExceptionHandler({UserTakenException.class, TaskTakenException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseError handleConflictExceptions(Exception ex, WebRequest request) {
    log.error("Error 409: Conflict");
    return new ResponseError(HttpStatus.CONFLICT, ex.getMessage());
  }
  @ExceptionHandler({HttpMessageNotReadableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleNotReadableExceptions(HttpMessageNotReadableException ex, WebRequest request){
    Pattern pattern = Pattern.compile("\\[(.*?)]");
    Matcher matcher = pattern.matcher(ex.getMessage());
    String errorMessage;
    if(matcher.find()){
      errorMessage=matcher.group(1);
    }
    else {
      errorMessage="Field cannot be empty";
    }
    log.error("Error 400: Bad request");
    return new ResponseError(HttpStatus.BAD_REQUEST, String.format("Invalid argument: %s", errorMessage));
  }
  @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleTypeMismatch(Exception ex) {
    log.error("Error 400: Bad request");
    return new ResponseError(HttpStatus.BAD_REQUEST, "Invalid argument!");
  }

}
