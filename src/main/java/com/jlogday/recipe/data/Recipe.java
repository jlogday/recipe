package com.jlogday.recipe.data;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Recipe {
    private int id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String name;
    private String description;
}
