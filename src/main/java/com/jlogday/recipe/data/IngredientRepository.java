package com.jlogday.recipe.data;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.opengamma.elsql.ElSql;
import com.opengamma.elsql.ElSqlConfig;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Builder
@RequiredArgsConstructor
@Slf4j
public class IngredientRepository {
    private enum Name {
        INSERT_INGREDIENT,
        UPDATE_INGREDIENT,
        FIND_BY_ID,
        FIND_BY_NAME,
        FIND_ALL_BY_NAMES,
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Ingredient.class);

    public int insert(Ingredient ingredient) {
        var params = new MapSqlParameterSource()
                .addValue("name", ingredient.getName());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.INSERT_INGREDIENT), params, keyHolder);
        if (count == 0) {
            log.info("existing record unmodified");
        } else if (count == 1) {
            log.info("inserted 1 row");
        } else if (count == 2) {
            log.info("updated 1 row");
        } else {
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue).orElse(-1);
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

    public Optional<Ingredient> findById(int id) {
        var params = new MapSqlParameterSource().addValue("id", id);
        return DataAccessUtils.optionalResult(jdbc.query(getSql(Name.FIND_BY_ID),
                params,
                (rs, row) -> Ingredient.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .name(rs.getString("name")).build()));
    }

    public Optional<Ingredient> findByName(@NonNull String name) {
        var params = new MapSqlParameterSource().addValue("name", name.toLowerCase());
        return DataAccessUtils.optionalResult(jdbc.query(getSql(Name.FIND_BY_NAME),
                params,
                (rs, row) -> Ingredient.builder()
                .id(rs.getInt("id"))
                .version(rs.getInt("version"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .name(rs.getString("name")).build()));
    }

    public List<Ingredient> findAllByNames(List<String> names) {
        var params = new MapSqlParameterSource().addValue("names", names);
        return jdbc.query(getSql(Name.FIND_ALL_BY_NAMES),
                params,
                (rs, row) -> Ingredient.builder()
                .id(rs.getInt("id"))
                .version(rs.getInt("version"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .name(rs.getString("name")).build());
    }

    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
