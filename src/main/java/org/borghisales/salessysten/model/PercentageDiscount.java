package org.borghisales.salessysten.model;

public class PercentageDiscount implements DiscountSrategy {
    private final double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double apply(double unitPrice, int quantity) {
        return unitPrice * (1 - percentage);
    }
}
