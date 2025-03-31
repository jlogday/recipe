package com.jlogday.recipe.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
        INSERT_INGREDIENT,
        UPDATE_INGREDIENT,
        FIND_BY_RECIPE_ID
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Ingredient.class);

    public int insert(Ingredient ingredient) {
        var params = new MapSqlParameterSource()
                .addValue("name", ingredient.getName());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.INSERT_INGREDIENT), params, keyHolder);
        if (count != 1) {
            log.error("error inserting record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return keyHolder.getKey().intValue();
    }

    public int update(Ingredient ingredient) {
        var params = new MapSqlParameterSource()
                .addValue("id", ingredient.getId())
                .addValue("version", ingredient.getVersion())
                .addValue("name", ingredient.getName());
        int count = jdbc.update(getSql(Name.UPDATE_INGREDIENT), params);
        if (count != 1) {
            log.error("error updating record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return count;
    }

    public List<Ingredient> findByRecipeId() {
        return jdbc.query(getSql(Name.FIND_BY_RECIPE_ID),
                (rs, row) -> Ingredient.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .name(rs.getString("name"))
                  .build());
    }


    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
