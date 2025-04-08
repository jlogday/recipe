package com.jlogday.recipe.service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlogday.recipe.IngredientDTO;
import com.jlogday.recipe.RecipeDTO;
import com.jlogday.recipe.data.Ingredient;
import com.jlogday.recipe.data.IngredientRepository;
import com.jlogday.recipe.data.MeasuredIngredient;
import com.jlogday.recipe.data.MeasuredIngredientRepository;
import com.jlogday.recipe.data.Recipe;
import com.jlogday.recipe.data.RecipeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private IngredientRepository ingredientRepo;
    @Autowired
    private MeasuredIngredientRepository measuredIngredientRepo;


    @Transactional
    public void createRecipe(RecipeDTO recipe) {
        int recipeId = recipeRepo.insert(Recipe.builder()
                .category(recipe.getCategory())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .build());

        // check if there are any existing ingredients with the given names
        var names = recipe.getIngredients().stream()
                .map(IngredientDTO::getName)
                .toList();

        // create a map of ingredient names to existing domain objects
        var map = ingredientRepo.findAllByNames(names).stream()
                .collect(Collectors.toMap(r -> r.getName(), Function.identity()));

        // insert each measured ingredient, also inserting the ingredient if it does not already exist
        recipe.getIngredients().forEach(i -> {
            int ingredientId = Optional.ofNullable(map.get(i.getName()))
                    .map(r -> r.getId())
                    .orElseGet(() -> ingredientRepo.insert(Ingredient.builder().name(i.getName()).build()));
            measuredIngredientRepo.insert(MeasuredIngredient.builder()
                    .recipeId(recipeId)
                    .ingredientId(ingredientId)
                    .quantity(i.getQuantity())
                    .build());
        });

    }

    public Optional<RecipeDTO> findRecipe(int id) {
        return recipeRepo.findById(id)
                .map(this::populateRecipe);
    }

    public RecipeDTO fetchRecipe(String name) {
        var recipe = recipeRepo.findByName(name).orElseThrow(); // TODO handle exception
        return populateRecipe(recipe);
    }

    public RecipeDTO fetchRecipe(int id) {
        var recipe = recipeRepo.findById(id).orElseThrow(); // TODO handle exception
        return populateRecipe(recipe);
    }

    private RecipeDTO populateRecipe(Recipe recipe) {
        var ingredients = recipeRepo.findIngredientsView(recipe.getId());
        return RecipeDTO.builder()
                .category(recipe.getCategory())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .ingredients(ingredients)
                .build();
    }
}
