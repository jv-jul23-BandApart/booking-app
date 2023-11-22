package com.bookingapp.service.impl;

import com.bookingapp.exception.TelegramException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private static final String START_COMMAND = "/start";

    @Value("${telegram.bot.username}")
    private String token;

    @Value("${telegram.bot.token}")
    private String name;

    @Override
    public String getBotUsername() {
        return "apart_booking_bot";
    }

    @Override
    public String getBotToken() {
        return "6520677831:AAHeP3D-GutImMOEcp5p0skxATIMJFqttmI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String userId = message.getChatId().toString();
            String firstName = message.getFrom().getFirstName();
            if (message.getText().equals(START_COMMAND)) {
                startCommandResponse(userId, firstName);
            } else {
                sendMessage(userId, "This command is unavailable!");
            }
        }
    }

    private void startCommandResponse(String chatId, String firstName) {
        String answer = "Hello, " + firstName + ", very glad to see you.";
        sendMessage(chatId, answer);
    }

    private void sendMessage(String chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramException("Message not send: ", e);
        }
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new TelegramException("Can't register bot :", e);
        }
    }
}
