package org.borghisales.salessysten.model;

public class BulkDiscount implements DiscountSrategy {

    private final int minQuantity;
    private final double percentage; // 0.10, 0.20, etc.

    public BulkDiscount(int minQuantity, double percentage) {
        this.minQuantity = minQuantity;
        this.percentage = percentage;
    }

    @Override
    public double apply(double unitPrice, int quantity) {
        if (quantity >= minQuantity) {
            return unitPrice * (1 - percentage);
        }
        // No se aplica descuento
        return unitPrice;
    }
}
