package com.bookingapp.service.impl;

import com.bookingapp.exception.TelegramException;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import com.bookingapp.service.NotificationService;
import jakarta.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl extends TelegramLongPollingBot
        implements NotificationService {
    private static final String START_COMMAND = "/start";

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Value("${telegram.bot.username}")
    private String name;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private void startCommand(String chatId, String firstName) {
        String answer = "Hello, " + firstName + ", very glad to see you.";
        sendMessage(chatId, answer);
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
                sendMessage(userId, "This command does not exist");
            }
        }
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
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(notification);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramException("Can't send message: ", e);
        }
    }

    @Override
    public void bookingToMessage(Booking booking, Accommodation accommodation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String message = """
               Added new reservation
               Accommodation Type: %s
               Check-in Date: %s
               Check-out Date: %s
               Customer Id: %d
                """;

        String notification = String.format(
                message,
                accommodation.getType(),
                booking.getCheckInDate().format(formatter),
                booking.getCheckOutDate().format(formatter),
                booking.getUser().getId()
        );

        userNotification(notification);
    }

    @Override
    public void bookingDeleteToMessage(Booking booking) {
        String message = """
            Booking (ID: %s) was deleted.
            Customer: %d
            Check-in: %s, Check-out: %s
                """;
        String notification = String.format(message, booking.getAccommodation().getType(),
                booking.getUser().getId(), booking.getCheckInDate(), booking.getCheckOutDate());
        userNotification(notification);
    }

    @Override
    public void paymentToMessage(Payment payment) {
        String messageToUser = """
            Booking (ID: %s) has been successfully paid for!
            Amount: $%s
            Payment Status: %s
                """;
        String notification = String.format(messageToUser, payment.getId(),
                payment.getAmountToPay(),
                payment.getStatus());
        userNotification(notification);
    }

    @Override
    public void paymentFailedToMessage(Payment payment) {
        String messageToUser = """
            Payment for booking (ID: %s) has been declined.
            Amount: $%s
            Payment Status: %s
                 """;
        String notification = String.format(messageToUser, payment.getId(),
                payment.getAmountToPay(),
                payment.getStatus());
        userNotification(notification);
    }

    @Override
    public void getMessageToExpiredBookings(List<Booking> bookings) {

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
