package com.english.words.bot.english_bot.repository;

import com.english.words.bot.english_bot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {
        Optional<User> findByChatId(Long chatId);

        List<User> findAll();
    }

