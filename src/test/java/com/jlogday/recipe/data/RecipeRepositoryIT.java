package com.jlogday.recipe.data;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackageClasses = RecipeRepository.class)
@Slf4j
public class RecipeRepositoryIT {
    @Autowired
    private RecipeRepository repo;

    @Test
    void loadAll() {
        var list = repo.findAll();
        log.info("loaded: {}", list);
    }

    @Test
    void createAndLoad() {
        var recipe = Instancio.create(Recipe.class);
        repo.save(recipe);
        var list = repo.findAll();
        log.info("loaded: {}", list);
    }

}
