package com.jlogday.recipe.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jlogday.recipe.data.Recipe;

import reactor.core.publisher.Mono;

@Component
public class RecipeClient {

    private final WebClient client;

    public RecipeClient(WebClient.Builder builder) {
        client = builder.baseUrl("http://localhost:8080").build();
    }

    public Mono<String> getRecipe() {
        return client.get().uri("/recipe").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Recipe.class)
                .map(Recipe::toString);
    }
}
