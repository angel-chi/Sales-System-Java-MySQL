package org.borghisales.salessysten.model.envio;

public class EnvioExpres extends Envio {

    public EnvioExpres() {
        super(150);
    }

    @Override
    public double calcularCosto() {
        return costoBase;
    }
}
