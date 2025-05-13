package com.jlogday.recipe.service;

import java.util.List;
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
import com.jlogday.recipe.data.Instruction;
import com.jlogday.recipe.data.InstructionRepository;
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
    private InstructionRepository instructionRepo;
    @Autowired
    private IngredientRepository ingredientRepo;
    @Autowired
    private MeasuredIngredientRepository measuredIngredientRepo;


    @Transactional
    public int createRecipe(RecipeDTO recipe) {
        int recipeId = recipeRepo.insert(Recipe.builder()
                .category(recipe.getCategory())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .build());

        recipe.getInstructions().forEach(i -> {
            instructionRepo.insert(Instruction.builder()
                    .recipeId(recipeId)
                    .value(i)
                    .build());
        });

        // check if there are any existing ingredients with the given names
        var names = recipe.getIngredients().stream()
                .map(IngredientDTO::getName)
                .toList();

        // create a map of ingredient names to existing domain objects
        var map = ingredientRepo.findAllByNames(names).stream()
                .collect(Collectors.toMap(Ingredient::getName, Function.identity()));

        // insert each measured ingredient, also inserting the ingredient if it does not already exist
        recipe.getIngredients().forEach(i -> {
            int ingredientId = Optional.ofNullable(map.get(i.getName()))
                    .map(Ingredient::getId)
                    .orElseGet(() -> ingredientRepo.insert(Ingredient.builder().name(i.getName()).build()));
            measuredIngredientRepo.insert(MeasuredIngredient.builder()
                    .recipeId(recipeId)
                    .ingredientId(ingredientId)
                    .quantity(i.getQuantity())
                    .note(i.getNote())
                    .build());
        });

        return recipeId;
    }

    public List<RecipeDTO> allRecipes() {
        return recipeRepo.findAll().stream()
                .map(this::mapRecipe)
                .toList();
    }

    public Optional<RecipeDTO> findRecipe(int id) {
        return recipeRepo.findById(id)
                .map(this::mapRecipe);
    }

    public RecipeDTO fetchRecipe(String name) {
        var recipe = recipeRepo.findByName(name).orElseThrow(); // TODO handle exception
        return mapRecipe(recipe);
    }

    public RecipeDTO fetchRecipe(int id) {
        var recipe = recipeRepo.findById(id).orElseThrow(); // TODO handle exception
        return mapRecipe(recipe);
    }

    private RecipeDTO mapRecipe(Recipe recipe) {
        var instructions = instructionRepo.findByRecipeId(recipe.getId()).stream()
                .map(Instruction::getValue)
                .toList();
        var ingredients = recipeRepo.findIngredientsView(recipe.getId());
        return RecipeDTO.builder()
                .id(recipe.getId())
                .category(recipe.getCategory())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .instructions(instructions)
                .ingredients(ingredients)
                .build();
    }
}
