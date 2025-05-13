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
public class InstructionRepository {
    private enum Name {
        INSERT,
        UPDATE,
        FIND_BY_RECIPE_ID
    }

    private final NamedParameterJdbcTemplate jdbc;

    private static final ElSql elsql = ElSql.of(ElSqlConfig.MYSQL, Instruction.class);

    public int insert(Instruction instruction) {
        var params = new MapSqlParameterSource()
                .addValue("recipe_id", instruction.getRecipeId())
                .addValue("value", instruction.getValue());
        var keyHolder = new GeneratedKeyHolder();
        int count = jdbc.update(getSql(Name.INSERT), params, keyHolder);
        if (count != 1) {
            log.error("error inserting record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return keyHolder.getKey().intValue();
    }

    public int update(Instruction instruction) {
        var params = new MapSqlParameterSource()
                .addValue("id", instruction.getId())
                .addValue("version", instruction.getVersion())
                .addValue("recipe_id", instruction.getRecipeId())
                .addValue("value", instruction.getValue());
        int count = jdbc.update(getSql(Name.UPDATE), params);
        if (count != 1) {
            log.error("error updating record");
            throw new IncorrectResultSizeDataAccessException(1, count);
        }

        return count;
    }

    public List<Instruction> findByRecipeId(int recipeId) {
        var params = new MapSqlParameterSource().addValue("recipe_id", recipeId);
        return jdbc.query(getSql(Name.FIND_BY_RECIPE_ID),
                params,
                (rs, row) -> Instruction.builder()
                  .id(rs.getInt("id"))
                  .version(rs.getInt("version"))
                  .created(rs.getTimestamp("created").toLocalDateTime())
                  .updated(rs.getTimestamp("updated").toLocalDateTime())
                  .recipeId(rs.getInt("recipe_id"))
                  .value(rs.getString("value"))
                  .build());
    }


    private String getSql(Name name) {
        var sql = elsql.getSql(name.toString());
        log.debug("{} : {}", name, sql);
        return sql;
    }
}
