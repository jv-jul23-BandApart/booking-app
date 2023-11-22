package com.bookingapp.controller;

import com.bookingapp.dto.payment.PaymentResponseDto;
import com.bookingapp.model.User;
import com.bookingapp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Payment", description = "Endpoints for payment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Completes payment by booking id",
            description = "completes payment")
    @GetMapping
    public RedirectView completePayment(@RequestParam("session_id") String sessionId) {
        return paymentService.completePayment(sessionId);
    }

    @Operation(summary = "Returns user's payment history",
            description = "Payment history")
    @GetMapping("/history")
    public List<PaymentResponseDto> completePayment(@RequestParam("user_id") Long userId) {
        return paymentService.getPaymentHistory(userId);
    }

    @Operation(summary = "Create payment",
            description = "Initiates payment sessions for booking transactions.")
    @PostMapping("/{bookingId}")
    public PaymentResponseDto createPayment(@PathVariable Long bookingId,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.createPayment(bookingId, user.getId());
    }

    @Operation(summary = "Success payment operation",
            description = "Initiates payment success status")
    @GetMapping("/success")
    public String successOperation(@RequestParam("session_id") String sessionId) {
        paymentService.setSuccessStatus(sessionId);
        return "Successful operation!";
    }

    @Operation(summary = "Canceled payment operation",
            description = "Initiates payment canceled status")
    @GetMapping("/canceled")
    public String canceledOperation() {
        return """
                Canceled operation!
                The payment can be made later (but the session is available for only 24 hours);
               """;
    }
}
