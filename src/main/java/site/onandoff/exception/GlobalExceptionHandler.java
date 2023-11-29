package site.onandoff.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import site.onandoff.util.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception) {
		return ResponseEntity.status(exception.getStatus())
			.body(ApiResponse.of(exception.getStatus(), exception.getMessage(), null));
	}

}
