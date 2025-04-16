package com.jlogday.recipe;

import java.util.ArrayList;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
class RecipeApplicationTests {
    @Autowired
    private WebTestClient client;

    @Test
    void allRecipes() throws Exception {
        var result = client.get().uri("/recipes").exchange().expectStatus().isOk().returnResult(String.class);
        log.info("response: {}", result.getResponseBody().single().block());
    }

    @Test
    void createAndFetch() throws Exception {
        var recipe = Instancio.of(RecipeDTO.class)
                .set(Select.field(RecipeDTO::getIngredients), new ArrayList<>())
                .create();
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        recipe.getIngredients().add(Instancio.create(IngredientDTO.class));
        var result = client.post()
            .uri("/recipes")
            .bodyValue(recipe)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().exists("Location")
            .returnResult(String.class);
        var location = result.getResponseHeaders().getLocation();
        log.info("location: {}", location);

        var getResult = client.get().uri(location).exchange().expectStatus().isOk().returnResult(String.class);
        log.info("response: {}", getResult.getResponseBody().single().block());
    }
}
