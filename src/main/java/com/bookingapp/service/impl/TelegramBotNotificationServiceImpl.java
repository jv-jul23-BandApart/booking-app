package com.bookingapp.service.impl;

import com.bookingapp.exception.TelegramException;
import com.bookingapp.service.NotificationService;
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
public class TelegramBotNotificationServiceImpl extends TelegramLongPollingBot
        implements NotificationService {
    private static final String START_COMMAND = "/start";

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String name;

    @Override
    public String getBotUsername() {
        return "band_apart_booking_bot";
    }

    @Override
    public String getBotToken() {
        return "6675755812:AAHyMHhAjOMkd05ovuDsISblpnGnGJilDlI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String userId = message.getChatId().toString();
            String firstName = message.getFrom().getFirstName();
            if (message.getText().equals(START_COMMAND)) {
                startCommand(userId, firstName);
            } else {
                sendMessage(userId, "This command is unavailable!");
            }
        }
    }

    private void startCommand(String chatId, String firstName) {
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

    @Override
    public void userNotification(String notification) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(sendMessage));
        sendMessage.setText(notification);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramException("Can't send message: ", e);
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
