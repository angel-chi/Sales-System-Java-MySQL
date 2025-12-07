package org.borghisales.salessysten.model;

public enum Discount {
    NONE(0.0),
    SILVER(0.05),
    GOLD(0.10),
    PLATINUM(0.15);

    private final double percentage;

    Discount(double percentage){
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

}
