package com.jlogday.recipe.data;

import static org.assertj.core.api.Assertions.assertThat;

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
        log.info("generated: {}", recipe);
        int id = repo.insert(recipe);
        var loaded = repo.findById(id).orElseThrow();
        log.info("loaded: {}", loaded);
        assertThat(loaded.getId()).isNotNull();
        assertThat(loaded.getCreated()).isNotNull();
        assertThat(loaded.getUpdated()).isNotNull();
        assertThat(loaded.getCategory()).isEqualTo(recipe.getCategory());
        assertThat(loaded.getName()).isEqualTo(recipe.getName());
        assertThat(loaded.getDescription()).isEqualTo(recipe.getDescription());
        assertThat(loaded.getInstructions()).isEqualTo(recipe.getInstructions());
    }

    @Test
    void update() {
        var recipe = Instancio.create(Recipe.class);
        log.info("generated: {}", recipe);
        int id = repo.insert(recipe);
        var loaded = repo.findById(id).orElseThrow();
        log.info("loaded: {}", loaded);

        loaded.setName("Updated Name");
        repo.update(loaded);

        var updated = repo.findById(id).orElseThrow();
        log.info("updated: {}", updated);

        assertThat(updated.getCreated()).isEqualTo(loaded.getCreated());
        assertThat(updated.getUpdated()).isAfter(loaded.getUpdated());
        //assertThat(updated.getVersion()).isGreaterThan(loaded.getVersion());
        assertThat(updated.getCategory()).isEqualTo(recipe.getCategory());
        assertThat(updated.getName()).isNotEqualTo(recipe.getName());
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getDescription()).isEqualTo(recipe.getDescription());
        assertThat(updated.getInstructions()).isEqualTo(recipe.getInstructions());
    }

}
