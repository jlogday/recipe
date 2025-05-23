package com.jlogday.recipe.handler;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jlogday.recipe.RecipeDTO;
import com.jlogday.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeHandler {
    private final RecipeService recipeService;

    public Mono<ServerResponse> getAllRecipes(ServerRequest request) {
        var recipes = recipeService.allRecipes();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(recipes);
    }

    public Mono<ServerResponse> getRecipeById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return recipeService.findRecipe(id)
                .map(recipe -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(recipe))
                .orElse(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> createRecipe(ServerRequest request) {
        var recipe = request.bodyToMono(RecipeDTO.class);
        return recipe.flatMap(r -> Mono.just(recipeService.createRecipe(r)))
                .flatMap(id -> ServerResponse.created(URI.create("/recipes/" + id)).build());
    }
}
