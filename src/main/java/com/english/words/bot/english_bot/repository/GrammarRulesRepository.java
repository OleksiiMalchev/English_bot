package com.english.words.bot.english_bot.repository;

import com.english.words.bot.english_bot.model.GrammarRule;
import com.english.words.bot.english_bot.model.Word;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GrammarRulesRepository extends CrudRepository<GrammarRule, Long> {
    @Query(value = "SELECT * FROM grammar_rules ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<GrammarRule> findRandomRule();
}
