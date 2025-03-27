package com.jlogday.recipe.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jlogday.recipe.data.Recipe;
import com.jlogday.recipe.data.RecipeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepository repo;

    public Optional<Recipe> findById(int id) {
        return repo.findById(id);
    }
}
