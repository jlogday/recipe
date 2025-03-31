package com.jlogday.recipe.service;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jlogday.recipe.data.RecipeRepository;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository repo;

    @InjectMocks
    private RecipeService svc;

    @Test
    void empty() {
        assertThat(svc).isNotNull();
    }
}
