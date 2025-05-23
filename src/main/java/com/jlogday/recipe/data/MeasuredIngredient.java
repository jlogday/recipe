package com.jlogday.recipe.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class MeasuredIngredient extends Base {
    private int recipeId;
    private int ingredientId;
    private String quantity;
    private String note;
    private int ordinal;
}
