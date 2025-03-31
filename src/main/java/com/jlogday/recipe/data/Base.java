package com.jlogday.recipe.data;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@SuperBuilder
public abstract class Base {
    private final Integer id;
    private final int version;
    private final LocalDateTime created;
    private final LocalDateTime updated;
}
