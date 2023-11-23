package com.bookingapp.service;

import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import java.util.List;

public interface NotificationService {

    void userNotification(String notification);

    void bookingToMessage(Booking booking, Accommodation accommodation);

    void bookingDeleteToMessage(Booking booking);

    void paymentToMessage(Payment payment);

    void paymentFailedToMessage(Payment payment);

    void getMessageToExpiredBookings(List<Booking> bookings);

    void getMessageToEndedBookings(List<Booking> bookings);
}
