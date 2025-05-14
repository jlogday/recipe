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
public class Instruction extends Base {
    private int recipeId;
    private String value;
    private int ordinal;
}
