package com.stakeit.service;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookService {

    private final BetService betService;

    public StripeWebhookService(BetService betService) {
        this.betService = betService;
    }

    public void handleEvent(Event event) {
        System.out.println("WEBHOOK SERVICE HANDLING: " + event.getType());

        try {
            if ("checkout.session.completed".equals(event.getType())) {
                System.out.println("CHECKOUT SESSION COMPLETED FOUND");
                handleCheckoutCompleted(event);
            }
        } catch (Exception e) {
            System.out.println("WEBHOOK HANDLING FAILED");
        }
    }

    private void handleCheckoutCompleted(Event event) {
        System.out.println("INSIDE handleCheckoutCompleted");

        StripeObject stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElseGet(() -> {
                    try {
                        return event.getDataObjectDeserializer().deserializeUnsafe();
                    } catch (EventDataObjectDeserializationException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (!(stripeObject instanceof Session session)) {
            System.out.println("NOT A CHECKOUT SESSION");
            return;
        }

        String betIdString = session.getMetadata().get("betId");
        String paymentType = session.getMetadata().get("paymentType");

        if ("CREATE_BET".equals(paymentType)) {
            betService.activateBetAfterPayment(Integer.valueOf(betIdString));
        }
    }
}