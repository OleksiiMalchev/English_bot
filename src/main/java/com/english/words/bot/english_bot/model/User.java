package com.english.words.bot.english_bot.model;

import jakarta.persistence.*;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private int frequency;
    private String preference = "words";
    private long lastSentTime;
    @Column(name = "active")
    private boolean isActive = true;



    public void setActive(boolean active) {
        isActive = active;
    }

    public User() {
    }

    public User(Long chatId) {
        this.chatId = chatId;
    }

    public User(Long chatId, int frequency) {
        this.chatId = chatId;
        this.frequency = frequency;
        this.lastSentTime = System.currentTimeMillis();
    }
    public boolean isActive() {
        return isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public long getLastSentTime() {
        return lastSentTime;
    }

    public void setLastSentTime(long lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public boolean shouldSend() {
        long currentTime = System.currentTimeMillis();
        long minutesSinceLastSent = (currentTime - lastSentTime) / (1000 * 60);
        if (minutesSinceLastSent >= frequency) {
            lastSentTime = currentTime; // обновляем время последней отправки
            return true;
        }
        return false;
    }
}
