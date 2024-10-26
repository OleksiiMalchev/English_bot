package com.english.words.bot.english_bot.service.impl;

import com.english.words.bot.english_bot.config.BotConfig;
import com.english.words.bot.english_bot.model.GrammarRule;
import com.english.words.bot.english_bot.model.User;
import com.english.words.bot.english_bot.model.Word;
import com.english.words.bot.english_bot.repository.GrammarRulesRepository;
import com.english.words.bot.english_bot.repository.UserRepository;
import com.english.words.bot.english_bot.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    final BotConfig botConfig;

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GrammarRulesRepository grammarRulesRepository;
    @Autowired
    private UserService userService;

    public TelegramBotService(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Registration"));
        listOfCommands.add(new BotCommand("/delete", "Delete your data"));
        listOfCommands.add(new BotCommand("/stop", "Stop studying"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    sendStudyOptionMenu(chatId, "Выберите, что вы хотите изучать:");
                    break;
                case "/delete":
                    deleteUserData(chatId);
                    break;
                case "/stop":
                    stopStudying(chatId);
                    break;
                case "Words":
                    saveUserPreference(chatId, "words");
                    showFrequencyMenu(chatId, "You chose to study words.");
                    break;
                case "Grammar Rules":
                    saveUserPreference(chatId, "rules");
                    showFrequencyMenu(chatId, "You chose to study grammar rules.");
                    break;
                // Далее обработка частоты отправки
                case "Once a minute":
                    saveFrequency(chatId, 0);
                    sendMessage(chatId, "You will receive updates once a minute.");
                    break;
                case "Once an hour":
                    saveFrequency(chatId, 1);
                    sendMessage(chatId, "You will receive updates once an hour.");
                    break;
                case "Once every 3 hours":
                    saveFrequency(chatId, 3);
                    sendMessage(chatId, "You will receive updates once every 3 hours.");
                    break;
                case "Once every 6 hours":
                    saveFrequency(chatId, 6);
                    sendMessage(chatId, "You will receive updates once every 6 hours.");
                    break;
                case "Once a day":
                    saveFrequency(chatId, 24);
                    sendMessage(chatId, "You will receive updates once a day.");
                    break;
                case "/word":
                    sendWord(chatId);
                    break;
                default:
                    sendMessage(chatId, "Command not recognized.");
                    break;
            }
        }
    }

    private void deleteUserData(long chatId) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            sendMessage(chatId, "Ваши данные успешно удалены.");
        } else {
            sendMessage(chatId, "Ваши данные не найдены.");
        }
    }
    private void stopStudying(long chatId) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
            sendMessage(chatId, "Изучение остановлено.");
        } else {
            sendMessage(chatId, "Ваши данные не найдены.");
        }
    }
    private void sendStudyOptionMenu(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Words");
        row.add("Grammar Rules");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void showFrequencyMenu(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Once a minute");
        row.add("Once an hour");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Once every 3 hours");
        row.add("Once every 6 hours");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Once a day");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Once a minute");
        row.add("Once an hour");
        keyboardRows.add(row);


        row = new KeyboardRow();
        row.add("Once every 3 hours");
        row.add("Once every 6 hours");
        keyboardRows.add(row);


        row = new KeyboardRow();
        row.add("Once a day");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendWord(long chatId) {
        Optional<Word> randomWord = wordRepository.findRandomWord();
        if (randomWord.isPresent()) {
            String messageText = "Ваше новое слово на сегодня: \n" +
                    "Слово: " + randomWord.get().getEnglishWord() + "\n" +
                    "Перевод: " + randomWord.get().getTranslation() + "\n" +
                    "Транскрипция: " + randomWord.get().getTranscription();
            sendMessage(chatId, messageText);
        } else {
            sendMessage(chatId, "К сожалению, сегодня нет нового слова.");
        }
    }

    public void sendGrammarRule(long chatId) {
        Optional<GrammarRule> randomRule = grammarRulesRepository.findRandomRule();
        if (randomRule.isPresent()) {
            GrammarRule rule = randomRule.get();
            String messageText = "Ваше новое правило грамматики на сегодня: \n" +
                    "Время: " + rule.getTenseName() + "\n" +
                    "Формула: " + rule.getRuleFormula() + "\n" +
                    "Объяснение: " + rule.getRuleExplanation() + "\n" +
                    "Пример 1: " + rule.getExample1() + "\n" +
                    "Пример 2: " + rule.getExample2() + "\n" +
                    "Пример 3: " + rule.getExample3();
            sendMessage(chatId, messageText);
        } else {
            sendMessage(chatId, "К сожалению, сегодня нет нового правила грамматики.");
        }
    }

    private void registerUser(Message msg) {
        Optional<User> byChatId = userRepository.findByChatId(msg.getChatId());
        if (byChatId.isEmpty()) {
           userService.saveUser(msg.getChatId());
           sendWelcomeMessage(msg.getChatId(), msg.getChat().getFirstName());
        } else {
            sendAlreadyRegisteredMessage(msg.getChatId());
        }
    }

    private void sendWelcomeMessage(long chatId, String firstName) {
        String welcomeMessage = "Привет, " + firstName + "! Добро пожаловать в English5000WordsBot. Вы успешно зарегистрированы!";
        sendMessage(chatId, welcomeMessage);
    }

    private void sendAlreadyRegisteredMessage(long chatId) {
        sendMessage(chatId, "Вы уже зарегистрированы!");
    }



    private void saveUserPreference(long chatId, String preference) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User(chatId);
            user.setChatId(chatId);
        }
        user.setPreference(preference);
        userRepository.save(user);
    }

    private void saveFrequency(long chatId, int frequency) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User(chatId,frequency);
            user.setChatId(chatId);
        }
        user.setFrequency(frequency);
        user.setLastSentTime(System.currentTimeMillis());
        userRepository.save(user);
    }

}
