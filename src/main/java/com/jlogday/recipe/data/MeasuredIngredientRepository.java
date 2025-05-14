package com.jlogday.recipe.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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
public class MeasuredIngredientRepository {
    private enum Name {
        INSERT,
        UPDATE,
        FIND_BY_RECIPE_ID
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, MeasuredIngredient.class);

    public int insert(MeasuredIngredient ingredient) {
        var params = new MapSqlParameterSource()
                .addValue("recipe_id", ingredient.getRecipeId())
                .addValue("ingredient_id", ingredient.getIngredientId())
                .addValue("quantity", ingredient.getQuantity())
                .addValue("note", ingredient.getNote())
                .addValue("ordinal", ingredient.getOrdinal());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.INSERT), params, keyHolder);
        if (count != 1) {
            log.error("error inserting record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return keyHolder.getKey().intValue();
    }

    public int update(MeasuredIngredient ingredient) {
        var params = getUpdateParams(ingredient);
        int count = jdbc.update(getSql(Name.UPDATE), params);
        if (count != 1) {
            log.error("error updating record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return count;
    }

    private SqlParameterSource getUpdateParams(MeasuredIngredient ingredient) {
        var params = new MapSqlParameterSource()
                .addValue("id", ingredient.getId())
                .addValue("version", ingredient.getVersion())
                .addValue("recipe_id", ingredient.getRecipeId())
                .addValue("ingredient_id", ingredient.getIngredientId())
                .addValue("quantity", ingredient.getQuantity())
                .addValue("note", ingredient.getNote())
                .addValue("ordinal", ingredient.getOrdinal());

        return params;
    }

    public void update(List<MeasuredIngredient> list) {
        var args = list.stream()
            .map(this::getUpdateParams)
            .toArray(MapSqlParameterSource[]::new);
        int[] counts = jdbc.batchUpdate(getSql(Name.UPDATE), args);
        log.debug("counts: {}", counts);
    }

    public List<MeasuredIngredient> findByRecipeId(int recipeId) {
        var params = new MapSqlParameterSource().addValue("recipe_id", recipeId);
        return jdbc.query(getSql(Name.FIND_BY_RECIPE_ID),
                params,
                (rs, row) -> MeasuredIngredient.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .recipeId(rs.getInt("recipe_id"))
                  .ingredientId(rs.getInt("ingredient_id"))
                  .quantity(rs.getString("quantity"))
                  .note(rs.getString("note"))
                  .ordinal(rs.getInt("ordinal"))
                  .build());
    }


    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
