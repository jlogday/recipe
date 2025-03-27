package com.jlogday.recipe.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jlogday.recipe.data.Recipe;
import com.jlogday.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeHandler {
    private final RecipeService recipeService;

    public Mono<ServerResponse> getRecipe(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return recipeService.findById(id)
                .map(recipe -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(recipe))
                .orElse(ServerResponse.notFound().build());

    }


    public Mono<ServerResponse> recipe(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(Recipe.builder().name("Hello").description("Sample Recipe").build()));
      }
}
