package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.dto.User;

import javax.validation.Valid;

@RestController
@Validated
public class UserController {
    @Autowired
    private RecipesService recipesService;
    @PostMapping("/api/register")
    public ResponseEntity register(@Valid @RequestBody User user) throws BadRequestException {
        recipesService.register(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
