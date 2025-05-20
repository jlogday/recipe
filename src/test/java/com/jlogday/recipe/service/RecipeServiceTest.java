package com.jlogday.recipe.service;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.jlogday.recipe.util.RecipeAssert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepo;
    @Mock
    private InstructionRepository instructionRepo;
    @Mock
    private IngredientRepository ingredientRepo;
    @Mock
    private MeasuredIngredientRepository measuredIngredientRepo;

    @InjectMocks
    private RecipeService svc;

    @Captor
    private ArgumentCaptor<Recipe> recipeCaptor;

    @Captor
    private ArgumentCaptor<Instruction> instructionCaptor;

    @Captor
    private ArgumentCaptor<Ingredient> ingredientCaptor;

    @Captor
    private ArgumentCaptor<MeasuredIngredient> measuredIngredientCaptor;


    @Test
    void createBrandNewRecipe() {
        final int ingredientCount = 3;
        final int instructionCount = 5;

        var ingredients = Instancio.ofList(IngredientDTO.class).size(ingredientCount).create();
        var instructions = Instancio.gen().string().mixedCase().minLength(10).maxLength(200).list(instructionCount);

        var recipe = Instancio.of(RecipeDTO.class)
                .set(Select.field(RecipeDTO::getIngredients), ingredients)
                .set(Select.field(RecipeDTO::getInstructions), instructions)
                .create();

        svc.createRecipe(recipe);

        verify(recipeRepo).insert(recipeCaptor.capture());
        verify(instructionRepo, times(instructionCount)).insert(instructionCaptor.capture());
        verify(ingredientRepo, times(ingredientCount)).insert(ingredientCaptor.capture());
        verify(measuredIngredientRepo, times(ingredientCount)).insert(measuredIngredientCaptor.capture());

        RecipeAssert.assertThat(recipeCaptor.getValue()).isEquivalentTo(recipe);
        RecipeAssert.assertInstructionsEquivalent(instructions, instructionCaptor.getAllValues());
        RecipeAssert.assertIngredientsEquivalent(ingredients, ingredientCaptor.getAllValues());
        RecipeAssert.assertMeasuredIngredientsEquivalent(ingredients, measuredIngredientCaptor.getAllValues());
    }

    @Test
    void createWithExistingIngredients() {
        final int existingIngredientCount = 3;
        final int totalIngredientCount = 5;
        final int newIngredientCount = totalIngredientCount - existingIngredientCount;
        final int instructionCount = 5;

        var existingIngredients = Instancio.ofList(IngredientDTO.class).size(existingIngredientCount).create();
        var newIngredients = Instancio.ofList(IngredientDTO.class).size(newIngredientCount).create();
        var allIngredients = Stream.concat(existingIngredients.stream(), newIngredients.stream()).toList();
        var instructions = Instancio.gen().string().mixedCase().minLength(10).maxLength(200).list(instructionCount);

        var allNames = allIngredients.stream().map(IngredientDTO::getName).toList();
        @SuppressWarnings("unchecked")
        var existingModel = (List<Ingredient>) existingIngredients.stream()
                .map(dto -> Ingredient.builder()
                        .name(dto.getName())
                        .id(Double.valueOf(Math.random() * 10000).intValue())
                        .build())
                .toList();
        when(ingredientRepo.findAllByNames(allNames)).thenReturn(existingModel);

        var recipe = Instancio.of(RecipeDTO.class)
                .set(Select.field(RecipeDTO::getIngredients), allIngredients)
                .set(Select.field(RecipeDTO::getInstructions), instructions)
                .create();

        svc.createRecipe(recipe);

        verify(recipeRepo).insert(recipeCaptor.capture());
        verify(instructionRepo, times(instructionCount)).insert(instructionCaptor.capture());
        verify(ingredientRepo, times(newIngredientCount)).insert(ingredientCaptor.capture());
        verify(measuredIngredientRepo, times(totalIngredientCount)).insert(measuredIngredientCaptor.capture());

        RecipeAssert.assertThat(recipeCaptor.getValue()).isEquivalentTo(recipe);
        RecipeAssert.assertIngredientsEquivalent(newIngredients, ingredientCaptor.getAllValues());
        RecipeAssert.assertMeasuredIngredientsEquivalent(allIngredients, measuredIngredientCaptor.getAllValues());
    }
}
