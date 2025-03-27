package com.jlogday.recipe.data;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.opengamma.elsql.ElSql;
import com.opengamma.elsql.ElSqlConfig;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class RecipeRepository {
    private enum Name {
        ALL_RECIPES, FIND_BY_ID, SAVE_RECIPE,
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Recipe.class);

    public void save(Recipe recipe) {
        log.debug("saving recipe: {}", recipe);
        final var now = LocalDateTime.now();
        var params = new MapSqlParameterSource().addValue("created", now).addValue("updated", now)
                .addValue("name", recipe.getName()).addValue("description", recipe.getDescription());
        int count = jdbc.update(elsql.getSql(Name.SAVE_RECIPE.toString()), params);
        log.info("updated {} rows", count);
    }

    public List<Recipe> findAll() {
        return jdbc.query(elsql.getSql(Name.ALL_RECIPES.toString()),
                (rs, row) -> Recipe.builder().id(rs.getInt("id")).created(rs.getTimestamp("created").toLocalDateTime())
                        .updated(rs.getTimestamp("updated").toLocalDateTime()).name(rs.getString("name"))
                        .description(rs.getString("description")).build());
    }
}
