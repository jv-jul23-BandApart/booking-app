package com.bookingapp.service;

import com.bookingapp.dto.payment.PaymentResponseDto;
import java.util.List;
import org.springframework.web.servlet.view.RedirectView;

public interface PaymentService {
    PaymentResponseDto createPayment(Long bookingId, Long userId);

    void setSuccessStatus(String sessionId);

    RedirectView completePayment(String sessionId);

    List<PaymentResponseDto> getPaymentHistory(Long userId);
}
