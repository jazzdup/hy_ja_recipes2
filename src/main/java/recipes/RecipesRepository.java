package recipes;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecipesRepository extends PagingAndSortingRepository<RecipeEntity, Long> {
    public List<RecipeEntity> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    public List<RecipeEntity> findByNameContainsIgnoreCaseOrderByDateDesc(String name);
}
