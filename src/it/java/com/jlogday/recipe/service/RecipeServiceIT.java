package com.jlogday.recipe.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jlogday.recipe.IngredientDTO;
import com.jlogday.recipe.RecipeDTO;
import com.jlogday.recipe.data.Ingredient;
import com.jlogday.recipe.data.IngredientRepository;
import com.jlogday.recipe.data.RecipeRepository;

import lombok.extern.slf4j.Slf4j;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackageClasses = {RecipeRepository.class, RecipeService.class})
@Slf4j
public class RecipeServiceIT {
    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private RecipeService svc;

    @Test
    void createBrandNewRecipe() {
        var recipe = Instancio.of(RecipeDTO.class)
                .set(Select.field(RecipeDTO::getIngredients), new ArrayList<>())
                .create();
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));

        int id = svc.createRecipe(recipe);
        recipe.setId(id);

        var loaded = svc.fetchRecipe(id);
        log.info("loaded: {}", loaded);
        assertThat(loaded).isEqualTo(recipe);
    }

    @Test
    void createWithExistingIngredients() {
        var existingIngredients = List.of(Instancio.create(IngredientDTO.class), Instancio.create(IngredientDTO.class));
        var ids = existingIngredients.stream()
                .map(i -> ingredientRepo.insert(Ingredient.builder().name(i.getName()).build()))
                .toList();
        log.debug("ingr ids {}", ids);

        var recipe = Instancio.of(RecipeDTO.class)
                .set(Select.field(RecipeDTO::getIngredients), new ArrayList<>())
                .create();
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(existingIngredients.get(0));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(existingIngredients.get(0));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));

        int id = svc.createRecipe(recipe);
        recipe.setId(id);

        var loaded = svc.fetchRecipe(id);
        log.info("loaded: {}", loaded);
        assertThat(loaded).isEqualTo(recipe);
    }

    //@Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void populate() {
        var recipe = RecipeDTO.builder()
                .category("Cocktails")
                .name("Old Fashioned")
                .description("The classic cocktail.")
                //.instructions("Add Cointreau and bitters to an old fashioned glass and stir well.")
                .build();
        recipe.addIngredient("Cointreau", "1 barspoon");
        recipe.addIngredient("Angostura Bitters", "4 dashes");
        recipe.addIngredient("Rye", "2 oz");

        recipe.addInstruction("Add all ingredients to a mixing glass");
        recipe.addInstruction("Add ice and stir 10-15 seconds");
        recipe.addInstruction("Strain into an old fashioned glass");
        recipe.addInstruction("Express the oil of an orange peel");

        svc.createRecipe(recipe);
    }
}
