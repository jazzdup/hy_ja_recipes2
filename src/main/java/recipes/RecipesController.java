package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.dto.Id;
import recipes.dto.Recipe;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@Validated
public class RecipesController {
    @Autowired
    private RecipesService recipesService;

    @PostMapping("/new")
    public Id postRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails userDetails) {
        return recipesService.save(recipe, userDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity putRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorizedException {
        recipesService.update(recipe, id, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipesService.get(id);
    }

    @GetMapping("/search")
    public List<Recipe> searchCategory(@RequestParam(required = false) String category,
                                       @RequestParam(required = false) String name)
            throws BadRequestException {
        if (StringUtils.hasText(category)) {
            return recipesService.findByCategory(category);
        } else if (StringUtils.hasText(name)) {
            return recipesService.findByName(name);
        } else {
            throw new BadRequestException();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorizedException {
        recipesService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
