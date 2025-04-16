package com.jlogday.recipe;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jlogday.recipe.handler.LocationHandler;
import com.jlogday.recipe.handler.RecipeHandler;

@Configuration
public class RecipeApplicationConfig {

    @Bean
    public RouterFunction<ServerResponse> route(RecipeHandler handler) {
        return RouterFunctions.route()
                .GET("/recipes", accept(MediaType.APPLICATION_JSON), handler::getAllRecipes)
                .GET("/recipes/{id}", accept(MediaType.APPLICATION_JSON), handler::getRecipeById)
                .POST("/recipes", accept(MediaType.APPLICATION_JSON), handler::createRecipe)
                .build();
    }

    // for testing angular integration
    @Bean
    public RouterFunction<ServerResponse> locationsRoute(LocationHandler handler) {
    return RouterFunctions.route()
            .GET("/locations", accept(MediaType.APPLICATION_JSON), handler::getAllLocations)
            .GET("/locations/{id}", accept(MediaType.APPLICATION_JSON), handler::getLocationById)
            .build();
    }

    @Bean
    public CorsWebFilter corsFilter() {

        var config = new CorsConfiguration();

        config.applyPermitDefaultValues();

        //config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

}
