package org.borghisales.salessysten.model.payment;

public class CashPayment extends Payment{

    public CashPayment (double amount){
        super(amount);
    }

    @Override
    public boolean authorize () {
        return amount > 0;
    }

    @Override
    public String getDescription (){
      return "Pago en efectivo por: $" + String.format("%.2f", amount);
    }
}
