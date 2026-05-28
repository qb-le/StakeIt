package com.stakeit.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    public String createCreateBetCheckoutSession(
            Integer betId,
            String title,
            BigDecimal betPrice
    ) throws Exception {
        Stripe.apiKey = stripeSecretKey;

        long amountInCents = betPrice
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/payment-success")
                .setCancelUrl(frontendUrl + "/payment-cancelled")
                .putMetadata("paymentType", "CREATE_BET")
                .putMetadata("betId", betId.toString())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Create bet: " + title)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }
}