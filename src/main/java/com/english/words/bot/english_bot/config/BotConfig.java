package com.english.words.bot.english_bot.config;

import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.slf4j.Logger;


@Configuration
@EnableScheduling
@Data
public class BotConfig {

    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String token;

    public void logBotConfig() {
        logger.info("Bot name: {}", botName);
        logger.info("Bot token: {}", token);
    }
}
