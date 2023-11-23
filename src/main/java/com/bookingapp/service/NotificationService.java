package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;

public interface NotificationService {

    void userNotification(String notification);

    void bookingToMessage(Booking booking, Accommodation accommodation);

    void bookingUnsuccessfulToMessage(Booking booking);

    void paymentToMessage(Payment payment);

    void paymentFailedToMessage(Payment payment);
}
