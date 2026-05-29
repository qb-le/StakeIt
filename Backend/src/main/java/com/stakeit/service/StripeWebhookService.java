package com.stakeit.service;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookService {

    private final BetService betService;

    public StripeWebhookService(BetService betService) {
        this.betService = betService;
    }

    public void handleEvent(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            handleCheckoutCompleted(event);
        }
    }

    private void handleCheckoutCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (session == null) {
            return;
        }

        String paymentType = session.getMetadata().get("paymentType");

        if ("CREATE_BET".equals(paymentType)) {
            Integer betId = Integer.valueOf(session.getMetadata().get("betId"));

            betService.activateBetAfterPayment(betId);
        }
    }
}