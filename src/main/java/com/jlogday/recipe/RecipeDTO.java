package com.jlogday.recipe;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private int id;
    private String category;
    private String name;
    private String description;
    private String instructions;
    @Builder.Default
    private final List<IngredientDTO> ingredients = new ArrayList<>();

    public RecipeDTO addIngredient(String name, String quantity) {
        ingredients.add(IngredientDTO.builder().name(name).quantity(quantity).build());
        return this;
    }
}
