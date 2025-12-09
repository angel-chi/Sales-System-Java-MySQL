package org.borghisales.salessysten.model;

public class CardPayment extends Payment {

    public CardPayment(double costoBase) {
        super(costoBase);
    }

    @Override
    public double calculateFinalAmount() {
        return costoBase * 1.03;
    }
}
