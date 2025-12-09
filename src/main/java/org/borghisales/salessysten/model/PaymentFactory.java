package org.borghisales.salessysten.model;

public class PaymentFactory {

    private PaymentFactory() {
    }

    public static Payment create(PaymentType type, double costoBase) {
        if (type == null) {
            return new CashPayment(costoBase);
        }

        return switch (type) {
            case EFECTIVO -> new CashPayment(costoBase);
            case TARJETA -> new CardPayment(costoBase);
        };
    }
}

