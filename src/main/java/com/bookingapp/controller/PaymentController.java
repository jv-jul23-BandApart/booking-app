package com.bookingapp.controller;

import com.bookingapp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment", description = "Endpoints for payment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Create payment",
            description = "Initiates payment sessions for booking transactions.")
    @PostMapping("/{bookingId}")
    public void createPayment(@PathVariable Long bookingId) {
        System.out.println("i am in");
        paymentService.createPayment(bookingId);
    }

    @Operation(summary = "Create payment",
            description = "Initiates payment sessions for booking transactions.")
    @GetMapping("/success")
    public String successOperation() {
        System.out.println("42142342");
        return "Successful operation!";
    }
}
