package com.jlogday.recipe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDTO {
    private String name;
    private String quantity;
}
