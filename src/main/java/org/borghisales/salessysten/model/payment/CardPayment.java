package org.borghisales.salessysten.model.payment;

public class CardPayment extends Payment {

    private final String lastFourDigits;

    public CardPayment (double amount, String lastFourDigits) {
        super(amount);
        this.lastFourDigits = lastFourDigits;
    }

    @Override
    public boolean authorize (){
        return amount > 0 && lastFourDigits != null && lastFourDigits.matches("\\d{4}");
    }

    @Override
    public  String getDescription(){
        return "Pago con tarjeta ****" + lastFourDigits + "por $" + String.format("%.2f", amount);
    }
}
