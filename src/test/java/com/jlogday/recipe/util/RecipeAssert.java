package com.jlogday.recipe.util;

import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AutoCloseableSoftAssertionsProvider;
import org.assertj.core.api.SoftAssertionsProvider;

import com.jlogday.recipe.IngredientDTO;
import com.jlogday.recipe.RecipeDTO;
import com.jlogday.recipe.data.Ingredient;
import com.jlogday.recipe.data.Instruction;
import com.jlogday.recipe.data.MeasuredIngredient;
import com.jlogday.recipe.data.Recipe;

public class RecipeAssert extends AbstractAssert<RecipeAssert, Recipe> {

    public static class RecipeSoftAssert extends AbstractSoftAssertions implements AutoCloseableSoftAssertionsProvider {
        public RecipeAssert assertThat(Recipe actual) {
            return proxy(RecipeAssert.class, Recipe.class, actual);
        }

        public static void assertSoftly(Consumer<RecipeSoftAssert> softly) {
            SoftAssertionsProvider.assertSoftly(RecipeSoftAssert.class, softly);
        }
    }

    public RecipeAssert(Recipe actual) {
        super(actual, RecipeAssert.class);
    }

    public static RecipeAssert assertThat(Recipe actual) {
        return new RecipeAssert(actual);
    }

    public RecipeAssert hasCategory(String category) {
        isNotNull();
        Assertions.assertThat(actual.getCategory()).as("category").isEqualTo(category);
        return this;
    }

    public RecipeAssert hasName(String name) {
        isNotNull();
        Assertions.assertThat(actual.getName()).as("name").isEqualTo(name);
        return this;
    }

    public RecipeAssert hasDescription(String description) {
        isNotNull();
        Assertions.assertThat(actual.getDescription()).as("description").isEqualTo(description);
        return this;
    }

    public RecipeAssert hasPhoto(String photo) {
        isNotNull();
        Assertions.assertThat(actual.getPhoto()).as("photo").isEqualTo(photo);
        return this;
    }

    public RecipeAssert isEquivalentTo(RecipeDTO dto) {
        RecipeSoftAssert.assertSoftly(softly -> {
            softly.assertThat(actual).hasCategory(dto.getCategory());
            softly.assertThat(actual).hasName(dto.getName());
            softly.assertThat(actual).hasDescription(dto.getDescription());
            softly.assertThat(actual).hasPhoto(dto.getPhoto());
        });

        return this;
    }

    public static void assertInstructionsEquivalent(List<String> expected, List<Instruction> actual) {
        final var index = new AtomicInteger();
        final var tuples = expected.stream()
            .map(inst -> tuple(inst, index.getAndIncrement()))
            .toList();
        Assertions.assertThat(actual)
            .extracting(Instruction::getValue, Instruction::getOrdinal)
            .containsExactlyElementsOf(tuples);

    }

    public static void assertIngredientsEquivalent(List<IngredientDTO> expected, List<Ingredient> actual) {
        final var names = expected.stream()
                .map(IngredientDTO::getName)
                .toList();
        Assertions.assertThat(actual)
            .extracting(Ingredient::getName)
            .containsExactlyElementsOf(names);
    }

    public static void assertMeasuredIngredientsEquivalent(List<IngredientDTO> expected,
            List<MeasuredIngredient> actual) {
        final var index = new AtomicInteger();
        final var tuples = expected.stream()
            .map(ingr -> tuple(ingr.getQuantity(), ingr.getNote(), index.getAndIncrement()))
            .toList();
        Assertions.assertThat(actual)
            .extracting(MeasuredIngredient::getQuantity, MeasuredIngredient::getNote, MeasuredIngredient::getOrdinal)
            .containsExactlyElementsOf(tuples);

    }
}
