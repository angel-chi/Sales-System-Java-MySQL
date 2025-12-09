package org.borghisales.salessysten.model.envio;

public class EnvioEconomico extends Envio {

    public EnvioEconomico() {
        super(80);
    }

    @Override
    public double calcularCosto() {
        return costoBase;
    }
}
