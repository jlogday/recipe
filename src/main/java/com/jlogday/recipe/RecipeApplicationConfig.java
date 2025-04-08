package com.jlogday.recipe;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jlogday.recipe.handler.RecipeHandler;

@Configuration
public class RecipeApplicationConfig {

    @Bean
    public RouterFunction<ServerResponse> route(RecipeHandler handler) {
        return RouterFunctions.route()
            .GET("/recipes/{id}", accept(MediaType.APPLICATION_JSON), handler::getRecipeById)
            .build();
    }
}
