package com.jlogday.recipe.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackageClasses = MeasuredIngredientRepository.class)
@Slf4j
public class MeasuredIngredientRepositoryIT {
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private IngredientRepository ingredientRepo;
    @Autowired
    private MeasuredIngredientRepository measuredIngredientRepo;

    @Test
    void findByRecipeId() {
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);

        IntStream.range(0, 7)
            .forEach(__ -> {
                var ingredient = Instancio.create(Ingredient.class);
                int ingredientId = ingredientRepo.insert(ingredient);

                var mi = Instancio.of(MeasuredIngredient.class)
                        .set(Select.field(MeasuredIngredient::getRecipeId), recipeId)
                        .set(Select.field(MeasuredIngredient::getIngredientId), ingredientId)
                        .create();
                measuredIngredientRepo.insert(mi);
            });

        var list = measuredIngredientRepo.findByRecipeId(recipeId);
        assertThat(list).hasSize(7);
    }


}
