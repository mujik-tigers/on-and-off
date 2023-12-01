package site.onandoff.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import site.onandoff.util.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception) {
		return ResponseEntity.status(exception.getStatus())
			.body(ApiResponse.of(exception.getStatus(), exception.getMessage(), null));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ApiResponse<Object> handleBindException(BindException exception) {
		return ApiResponse.of(HttpStatus.BAD_REQUEST, null,
			exception.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.groupingBy(FieldError::getField))
				.entrySet().stream()
				.map(error -> {
					Map<String, Object> fieldError = new HashMap<>();
					fieldError.put("field", error.getKey());
					fieldError.put("message", error.getValue().stream()
						.map(DefaultMessageSourceResolvable::getDefaultMessage)
						.collect(Collectors.joining(", ")));
					return fieldError;
				})
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ApiResponse<Object> handleConstraintViolationException(ConstraintViolationException exception) {
		return ApiResponse.of(HttpStatus.BAD_REQUEST, null,
			exception.getConstraintViolations().stream()
				.collect(Collectors.groupingBy(v -> parseFieldNameFrom(v.getPropertyPath())))
				.entrySet().stream()
				.map(error -> {
					Map<String, Object> fieldError = new HashMap<>();
					fieldError.put("field", error.getKey());
					fieldError.put("message", error.getValue().stream()
						.map(ConstraintViolation::getMessage)
						.collect(Collectors.joining(", ")));
					return fieldError;
				})
		);
	}

	private String parseFieldNameFrom(Path propertyPath) {
		String[] splitPath = propertyPath.toString().split("\\.");
		return splitPath[splitPath.length - 1];
	}

}
