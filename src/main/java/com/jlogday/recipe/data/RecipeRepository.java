package com.jlogday.recipe.data;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.jlogday.recipe.IngredientDTO;
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
        ALL_RECIPES,
        FIND_BY_ID,
        FIND_BY_NAME,
        INSERT_RECIPE,
        UPDATE_RECIPE,
        INGREDIENT_VIEW,
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Recipe.class);

    public int insert(Recipe recipe) {
        var params = new MapSqlParameterSource()
                .addValue("category", recipe.getCategory())
                .addValue("name", recipe.getName())
                .addValue("description", recipe.getDescription());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.INSERT_RECIPE), params, keyHolder);
        if (count != 1) {
            log.error("error inserting record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return keyHolder.getKey().intValue();
    }

    public int update(Recipe recipe) {
        var params = new MapSqlParameterSource()
                .addValue("id", recipe.getId())
                .addValue("version", recipe.getVersion())
                .addValue("category", recipe.getCategory())
                .addValue("name", recipe.getName())
                .addValue("description", recipe.getDescription());
        int count = jdbc.update(getSql(Name.UPDATE_RECIPE), params);
        if (count != 1) {
            log.error("error updating record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return count;
    }

    public Optional<Recipe> findById(int id) {
        var params = new MapSqlParameterSource().addValue("id", id);
        return DataAccessUtils.optionalResult(jdbc.query(getSql(Name.FIND_BY_ID),
                params,
                (rs, row) -> Recipe.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .category(rs.getString("category"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description"))
                  .build()));
    }

    public Optional<Recipe> findByName(String name) {
        var params = new MapSqlParameterSource().addValue("name", name);
        return DataAccessUtils.optionalResult(jdbc.query(getSql(Name.FIND_BY_NAME),
                params,
                (rs, row) -> Recipe.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .category(rs.getString("category"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description"))
                  .build()));
    }

    public List<Recipe> findAll() {
        return jdbc.query(getSql(Name.ALL_RECIPES),
                (rs, row) -> Recipe.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .category(rs.getString("category"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description"))
                  .build());
    }

    public List<IngredientDTO> findIngredientsView(int recipeId) {
        var params = new MapSqlParameterSource().addValue("recipe_id", recipeId);
        return jdbc.query(getSql(Name.INGREDIENT_VIEW),
                params,
                (rs, row) -> IngredientDTO.builder()
                  .name(rs.getString("name"))
                  .quantity(rs.getString("quantity")).build());
    }

    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
