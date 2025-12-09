package org.borghisales.salessysten.model;

public class CashPayment extends Payment {

    public CashPayment(double costoBase) {
        super(costoBase);
    }

    @Override
    public double calculateFinalAmount() {
        return costoBase * 0.95;
    }
}
