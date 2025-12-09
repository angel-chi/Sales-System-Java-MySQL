package org.borghisales.salessysten.model;

public abstract class Payment {

    protected final double costoBase;

    protected Payment(double costoBase) {
        this.costoBase = costoBase;
    }

    public double getBaseAmount() {
        return costoBase;
    }

    public abstract double calculateFinalAmount();
}
