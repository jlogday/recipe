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
public class Ingredient extends Base {
    private String name;
}
