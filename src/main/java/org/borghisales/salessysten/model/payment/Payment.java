package org.borghisales.salessysten.model.payment;

public abstract class Payment {

    protected final double amount;

    protected Payment (double amount) {
        this.amount = amount;
    }

    public double getAmount (){
        return amount;
    }

    public abstract boolean authorize();

    public abstract String getDescription();
}
