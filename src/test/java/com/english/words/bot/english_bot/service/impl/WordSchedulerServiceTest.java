package com.english.words.bot.english_bot.service.impl;

import com.english.words.bot.english_bot.model.User;
import com.english.words.bot.english_bot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class WordSchedulerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TelegramBotService telegramBotService;

    @InjectMocks
    private WordSchedulerService wordSchedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long chatId, String preference, int frequencyInHours, long lastSentTime) {
        User user = new User();
        user.setChatId(chatId);
        user.setPreference(preference);
        user.setFrequency(frequencyInHours);
        user.setLastSentTime(lastSentTime);
        return user;
    }

    @Test
    void scheduleWords_shouldSendWordWhenUserPrefersWordsAndIntervalPassed() {
        User user = createUser(12345L, "words", 1,
                System.currentTimeMillis() - 3600000);
        when(userRepository.findAll()).thenReturn(List.of(user));

        wordSchedulerService.scheduleWords();

        verify(telegramBotService, times(1)).sendWord(12345L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void scheduleWords_shouldSendGrammarRuleWhenUserPrefersRulesAndIntervalPassed() {
        User user = createUser(67890L, "rules", 2,
                System.currentTimeMillis() - 7200000);
        when(userRepository.findAll()).thenReturn(List.of(user));

        wordSchedulerService.scheduleWords();

        verify(telegramBotService, times(1)).sendGrammarRule(67890L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void scheduleWords_shouldNotSendMessageWhenIntervalNotPassed() {
        User user = createUser(112233L, "words", 1,
                System.currentTimeMillis() - 1800000);
        when(userRepository.findAll()).thenReturn(List.of(user));
        wordSchedulerService.scheduleWords();
        verify(telegramBotService, never()).sendWord(anyLong());
        verify(telegramBotService, never()).sendGrammarRule(anyLong());
        verify(userRepository, never()).save(user);
    }

    @Test
    void scheduleWords_shouldHandleEmptyUserList() {
        when(userRepository.findAll()).thenReturn(List.of());
        wordSchedulerService.scheduleWords();
        verifyNoInteractions(telegramBotService);
        verify(userRepository, never()).save(any());
    }
}
