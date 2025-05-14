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
        final int count = 7;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);

        IntStream.range(0, count)
            .forEach(i -> {
                var ingredient = Instancio.create(Ingredient.class);
                int ingredientId = ingredientRepo.insert(ingredient);

                var mi = Instancio.of(MeasuredIngredient.class)
                        .set(Select.field(MeasuredIngredient::getRecipeId), recipeId)
                        .set(Select.field(MeasuredIngredient::getIngredientId), ingredientId)
                        .set(Select.field(MeasuredIngredient::getOrdinal), i)
                        .create();
                measuredIngredientRepo.insert(mi);
            });

        var list = measuredIngredientRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }
    }

    @Test
    void ordinal() {
        final int count = 7;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);

        IntStream.range(0, count)
            .forEach(i -> {
                var ingredient = Instancio.create(Ingredient.class);
                int ingredientId = ingredientRepo.insert(ingredient);

                // note that the ordinal is in reverse order, from (count - 1) down to 0
                var mi = Instancio.of(MeasuredIngredient.class)
                        .set(Select.field(MeasuredIngredient::getRecipeId), recipeId)
                        .set(Select.field(MeasuredIngredient::getIngredientId), ingredientId)
                        .set(Select.field(MeasuredIngredient::getOrdinal), (count - 1) - i)
                        .create();
                measuredIngredientRepo.insert(mi);
            });

        var list = measuredIngredientRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }
    }

    @Test
    void reorder() {
        final int count = 4;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);


        IntStream.range(0, count)
            .forEach(i -> {
                var ingredient = Instancio.create(Ingredient.class);
                int ingredientId = ingredientRepo.insert(ingredient);

                var mi = Instancio.of(MeasuredIngredient.class)
                        .set(Select.field(MeasuredIngredient::getRecipeId), recipeId)
                        .set(Select.field(MeasuredIngredient::getIngredientId), ingredientId)
                        .set(Select.field(MeasuredIngredient::getOrdinal), i)
                        .create();
                measuredIngredientRepo.insert(mi);
            });

        var list = measuredIngredientRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }

        // swap the 2nd and 3rd ingredients
        var ing = list.get(2);
        assertThat(ing.getOrdinal()).isEqualTo(2);
        final int original2Id = ing.getIngredientId();
        ing.setOrdinal(3);

        ing = list.get(3);
        assertThat(ing.getOrdinal()).isEqualTo(3);
        final int original3Id = ing.getIngredientId();
        ing.setOrdinal(2);

        measuredIngredientRepo.update(list);

        list = measuredIngredientRepo.findByRecipeId(recipeId);
        log.info("list after reorder: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }

        // insure that the 2nd and 3rd ingredients were swapped
        assertThat(list.get(3).getIngredientId()).isEqualTo(original2Id);
        assertThat(list.get(2).getIngredientId()).isEqualTo(original3Id);
    }
}
