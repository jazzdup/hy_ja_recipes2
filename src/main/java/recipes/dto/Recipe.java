package recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotEmpty
    @Size(min = 1, message="Must have at least 1 ingredient")
    private List<String> ingredients;
    @NotEmpty
    @Size(min = 1, message="Must have >0 dirs")
    private List<String> directions;
    @NotBlank
    private String category;
    private LocalDateTime date;
}
