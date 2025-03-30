package com.jlogday.recipe.data;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class Base {
    private int id;
    private int version;
    private LocalDateTime created;
    private LocalDateTime updated;
}
