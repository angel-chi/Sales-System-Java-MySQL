package org.borghisales.salessysten.model;

public interface DiscountSrategy {
    double apply(double unitPrice, int quantity);
}
