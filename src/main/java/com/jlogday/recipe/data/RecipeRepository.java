package com.jlogday.recipe.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
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
public class RecipeRepository {
    private enum Name {
        ALL_RECIPES,
        FIND_BY_ID,
        SAVE_RECIPE,
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Recipe.class);

    public int insert(Recipe recipe) {
        final var now = LocalDateTime.now();
        var params = new MapSqlParameterSource()
                .addValue("created", now)
                .addValue("updated", now)
                .addValue("category", recipe.getCategory())
                .addValue("name", recipe.getName())
                .addValue("description", recipe.getDescription());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.SAVE_RECIPE), params, keyHolder);
        log.info("updated {} rows", count);
        return keyHolder.getKey().intValue();
    }

    public Optional<Recipe> findById(int id) {
        var params = new MapSqlParameterSource().addValue("id", id);
        return DataAccessUtils.optionalResult(jdbc.query(getSql(Name.FIND_BY_ID),
                params,
                (rs, row) -> Recipe.builder()
                  .id(rs.getInt("id"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .category(rs.getString("category"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description")).build()));
    }

    public List<Recipe> findAll() {
        return jdbc.query(getSql(Name.ALL_RECIPES),
                (rs, row) -> Recipe.builder()
                  .id(rs.getInt("id"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .category(rs.getString("category"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description")).build());
    }

    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
