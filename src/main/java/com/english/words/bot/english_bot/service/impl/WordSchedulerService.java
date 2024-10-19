package com.english.words.bot.english_bot.service.impl;

import com.english.words.bot.english_bot.model.User;
import com.english.words.bot.english_bot.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordSchedulerService {
    private final UserRepository userRepository;
    private final TelegramBotService telegramBotService;

    public WordSchedulerService(UserRepository userRepository, TelegramBotService telegramBotService) {
        this.userRepository = userRepository;
        this.telegramBotService = telegramBotService;
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleWords() {
        List<User> users = userRepository.findAll();
        long currentTime = System.currentTimeMillis();
        for (User user : users) {
            long timeSinceLastSent = currentTime - user.getLastSentTime();
            long interval = user.getFrequency() * 3600000;
            if (timeSinceLastSent >= interval) {
                if ("words".equals(user.getPreference())) {
                    telegramBotService.sendWord(user.getChatId());
                } else if ("rules".equals(user.getPreference())) {
                    telegramBotService.sendGrammarRule(user.getChatId());
                }
                user.setLastSentTime(currentTime);
                userRepository.save(user);
            }
        }
    }
}