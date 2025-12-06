package org.borghisales.salessysten.model;

public class BulkDiscount implements DiscountSrategy {

    private final int minQuantity;
    private final double percentage;

    public BulkDiscount(int minQuantity, double percentage) {
        this.minQuantity = minQuantity;
        this.percentage = percentage;
    }

    @Override
    public double apply(double unitPrice, int quantity) {
        if (quantity >= minQuantity) {
            return unitPrice * (1 - percentage);
        }
        return unitPrice;
    }
}
