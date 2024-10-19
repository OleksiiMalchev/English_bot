package com.english.words.bot.english_bot.model;

import jakarta.persistence.*;

@Table(name = "grammar_rules")
@Entity
public class GrammarRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tense_name")
    private String tenseName;

    @Column(name = "rule_formula", columnDefinition = "TEXT")
    private String ruleFormula;

    @Column(name = "rule_explanation", columnDefinition = "TEXT")
    private String ruleExplanation;

    @Column(name = "example_1", columnDefinition = "TEXT")
    private String example1;

    @Column(name = "example_2", columnDefinition = "TEXT")
    private String example2;

    @Column(name = "example_3", columnDefinition = "TEXT")
    private String example3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenseName() {
        return tenseName;
    }

    public void setTenseName(String tenseName) {
        this.tenseName = tenseName;
    }

    public String getRuleFormula() {
        return ruleFormula;
    }

    public void setRuleFormula(String ruleFormula) {
        this.ruleFormula = ruleFormula;
    }

    public String getRuleExplanation() {
        return ruleExplanation;
    }

    public void setRuleExplanation(String ruleExplanation) {
        this.ruleExplanation = ruleExplanation;
    }

    public String getExample1() {
        return example1;
    }

    public void setExample1(String example1) {
        this.example1 = example1;
    }

    public String getExample2() {
        return example2;
    }

    public void setExample2(String example2) {
        this.example2 = example2;
    }

    public String getExample3() {
        return example3;
    }

    public void setExample3(String example3) {
        this.example3 = example3;
    }
}