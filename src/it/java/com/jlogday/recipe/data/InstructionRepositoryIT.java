package com.jlogday.recipe.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ComponentScan(basePackageClasses = InstructionRepository.class)
@Slf4j
public class InstructionRepositoryIT {
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private InstructionRepository instructionRepo;

    @Test
    void findByRecipeId() {
        final int count = 7;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);

        IntStream.range(0, count)
            .forEach(i -> {
                var instruction = Instancio.of(Instruction.class)
                        .set(Select.field(Instruction::getRecipeId), recipeId)
                        .set(Select.field(Instruction::getOrdinal), i)
                        .create();
                instructionRepo.insert(instruction);
            });

        var list = instructionRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }
    }

    @Test
    void ordinal() {
        final int count = 7;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);

        IntStream.range(0, count)
            .forEach(i -> {
                // note that the ordinal is in reverse order, from (count - 1) down to 0
                var instruction = Instancio.of(Instruction.class)
                        .set(Select.field(Instruction::getRecipeId), recipeId)
                        .set(Select.field(Instruction::getOrdinal), (count - 1) - i)
                        .create();
                instructionRepo.insert(instruction);
            });

        var list = instructionRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }
    }

    @Test
    void reorder() {
        final int count = 4;
        var recipe = Instancio.create(Recipe.class);
        int recipeId = recipeRepo.insert(recipe);


        IntStream.range(0, count)
            .forEach(i -> {
                var instruction = Instancio.of(Instruction.class)
                        .set(Select.field(Instruction::getRecipeId), recipeId)
                        .set(Select.field(Instruction::getOrdinal), i)
                        .create();
                instructionRepo.insert(instruction);
            });

        var list = instructionRepo.findByRecipeId(recipeId);
        log.info("list: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }

        // swap the 2nd and 3rd instructions
        var inst = list.get(2);
        assertThat(inst.getOrdinal()).isEqualTo(2);
        final String original2Value = inst.getValue();
        inst.setOrdinal(3);

        inst = list.get(3);
        assertThat(inst.getOrdinal()).isEqualTo(3);
        final String original3Value = inst.getValue();
        inst.setOrdinal(2);

        instructionRepo.update(list);

        list = instructionRepo.findByRecipeId(recipeId);
        log.info("list after reorder: {}", list);
        assertThat(list).hasSize(count);
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getOrdinal()).isEqualTo(i);
        }

        // insure that the 2nd and 3rd instructions were swapped
        assertThat(list.get(3).getValue()).isEqualTo(original2Value);
        assertThat(list.get(2).getValue()).isEqualTo(original3Value);
    }
}
