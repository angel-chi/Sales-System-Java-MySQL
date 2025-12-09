package org.borghisales.salessysten.util;

public enum DiscountRate {
    CERO (0.0),
    DIEZ (0.10),
    QUINCE (0.15),
    VEINTE (0.20),
    CINCUENTA (0.50);

    private final double rate;

    DiscountRate(double rate){
        this.rate = rate;
    }
    public double getRate(){
        return rate;
    }

}
