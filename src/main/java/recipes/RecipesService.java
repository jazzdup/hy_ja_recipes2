package recipes;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.dto.Id;
import recipes.dto.Recipe;
import recipes.dto.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Bean
    public PasswordEncoder getEncoder() {
        //        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    public Id save(Recipe recipe, UserDetails userDetails) {
        RecipeEntity recipeEntity = modelMapper.map(recipe, RecipeEntity.class);
        UserEntity userEntity = userRepository.findUserByEmail(userDetails.getUsername());
        recipeEntity.setUserEntity(userEntity);
        RecipeEntity save = recipesRepository.save(recipeEntity);
        return new Id(save.getId());
    }

    public Recipe get(Long id) {
        RecipeEntity recipeEntity = recipesRepository.findById(id).orElseThrow();
        return modelMapper.map(recipeEntity, Recipe.class);
    }

    public Recipe delete(Long id, UserDetails userDetails) throws UnauthorizedException {
        RecipeEntity recipeEntity = recipesRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
        if (!recipeEntity.getUserEntity().getEmail().equals(userDetails.getUsername())) {
            throw new UnauthorizedException();
        }
        Recipe recipe = modelMapper.map(recipeEntity, Recipe.class); //must convert before delete!!
        recipesRepository.deleteById(id);
        return recipe;
    }

    public void update(Recipe recipe, Long id, UserDetails userDetails) throws UnauthorizedException {
        //check it's there first
        RecipeEntity recipeEntity = recipesRepository.findById(id).orElseThrow();
        if (!recipeEntity.getUserEntity().getEmail().equals(userDetails.getUsername())) {
            throw new UnauthorizedException();
        }
        RecipeEntity updatedRecipeEntity = modelMapper.map(recipe, RecipeEntity.class);
        UserEntity userEntity = userRepository.findUserByEmail(userDetails.getUsername());
        updatedRecipeEntity.setUserEntity(userEntity);
        updatedRecipeEntity.setId(id);
        updatedRecipeEntity.setDate(LocalDateTime.now());
        recipesRepository.save(updatedRecipeEntity);
    }

    public List<Recipe> findByCategory(String category) {
        return recipesRepository.findByCategoryIgnoreCaseOrderByDateDesc(
                category).stream().map(recipeEntity -> modelMapper.map(recipeEntity, Recipe.class))
                .collect(Collectors.toList());
    }

    public List<Recipe> findByName(String name) {
        return recipesRepository.findByNameContainsIgnoreCaseOrderByDateDesc(
                        name).stream().map(recipeEntity -> modelMapper.map(recipeEntity, Recipe.class))
                .collect(Collectors.toList());
    }

    public void register(User user) throws BadRequestException {
        UserEntity userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByEmail != null) {
            throw new BadRequestException();
        }else{
            UserEntity userToSave = modelMapper.map(user, UserEntity.class);
            userToSave.setPassword(getEncoder().encode(user.getPassword()));
            userRepository.save(userToSave);
        }
    }
}
