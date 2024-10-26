package com.english.words.bot.english_bot.service.impl;

import com.english.words.bot.english_bot.model.User;
import com.english.words.bot.english_bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void saveUser(Long chatId ) {
        User user = new User(chatId);
        userRepository.save(user);
    }
}
