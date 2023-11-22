package com.bookingapp.stripe;

import static com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData.builder;

import com.bookingapp.model.Booking;
import com.bookingapp.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeSessionProvider {
    private static final String CURRENCY = "usd";
    @Value("${stripe.secret.key}")
    private String secretKey;
    @Value("${local.domain}")
    private String localDomain;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Session createSession(Payment payment, Booking booking) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(localDomain + "/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(localDomain + "/cancel")
                .setExpiresAt(10L)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(CURRENCY)
                                .setUnitAmountDecimal(payment.getAmountToPay())
                                .setProductData(builder()
                                        .setName("Payment")
                                        .setDescription(booking.getAccommodation()
                                                .getType() + " booking")
                                        .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .build();
        return Session.create(params);
    }
}
