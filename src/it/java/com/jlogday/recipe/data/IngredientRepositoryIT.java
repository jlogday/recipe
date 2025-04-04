package com.jlogday.recipe.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
@ComponentScan(basePackageClasses = IngredientRepository.class)
@Slf4j
public class IngredientRepositoryIT {
    @Autowired
    private IngredientRepository repo;

    @Test
    void createAndLoad() {
        var ingredient = Instancio.create(Ingredient.class);
        log.info("generated: {}", ingredient);
        int id = repo.insert(ingredient);
        var loaded = repo.findById(id).orElseThrow();
        log.info("loaded: {}", loaded);
        assertThat(loaded.getId()).isNotNull();
        assertThat(loaded.getCreated()).isNotNull();
        assertThat(loaded.getUpdated()).isNotNull();
        assertThat(loaded.getName()).isEqualTo(ingredient.getName());
    }

    @Test
    void findByName() {
        var ingredient = Ingredient.builder().name("Lime Juice").build();
        log.info("generated: {}", ingredient);
        repo.insert(ingredient);
        var loaded = repo.findByName("Lime Juice").orElseThrow();
        log.info("loaded: {}", loaded);
        assertThat(loaded.getId()).isNotNull();
        assertThat(loaded.getCreated()).isNotNull();
        assertThat(loaded.getUpdated()).isNotNull();
        assertThat(loaded.getName()).isEqualTo(ingredient.getName());

        var variant1 = repo.findByName("lime juice").orElseThrow();
        assertThat(variant1).isEqualTo(loaded);

        var variant2 = repo.findByName("LIME JUICE").orElseThrow();
        assertThat(variant2).isEqualTo(loaded);
    }

    @Test
    void nameIsCaseInsensitive() {
        var id = repo.insert(Ingredient.builder().name("Lime Juice").build());
        int updateId = repo.insert(Ingredient.builder().name("Lime juice").build());
        assertThat(updateId).isEqualTo(-1);
        var loaded = repo.findByName("Lime juice").orElseThrow();
        assertThat(loaded.getId()).isEqualTo(id);
        assertThat(loaded.getName()).isEqualTo("Lime Juice");
    }


    @Test
    void findAllByNames() {
        var names = List.of("Lime Juice", "Simple Syrup", "Rum");
        names.forEach(name -> repo.insert(Ingredient.builder().name(name).build()));

        var list = repo.findAllByNames(names);
        log.info("list: {}", list);
        assertThat(list).hasSize(3);
    }
}
