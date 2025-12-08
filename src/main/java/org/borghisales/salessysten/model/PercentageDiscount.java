package org.borghisales.salessysten.model;

public class PercentageDiscount implements DiscountSrategy {
    private final double percentage; // 0.10 para 10%, 0.20 para 20%

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double apply(double unitPrice, int quantity) {
        // Descuento sobre el PRECIO UNITARIO
        return unitPrice * (1 - percentage);
    }
}
