package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;

public interface NotificationService {

    void userNotification(String notification);

    void bookingToMessage(Booking booking, Accommodation accommodation);

    void bookingUnsuccessfulToMessage(Booking booking);

    void paymentToMessage();

    void paymentFailedToMessage();
}
