package recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class, RecipeNotFoundException.class})
    public ResponseEntity handleNoSuchElementException(Exception ex, WebRequest request) {
        return ResponseEntity.notFound().build();
        //probably add typical <ErrorMessage>
    }
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity handleBadRequest(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedRequest(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
