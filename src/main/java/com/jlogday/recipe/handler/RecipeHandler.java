package com.jlogday.recipe.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jlogday.recipe.data.Recipe;

import reactor.core.publisher.Mono;

@Component
public class RecipeHandler {

    public Mono<ServerResponse> recipe(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(Recipe.builder().name("Hello").description("Sample Recipe").build()));
      }
}
