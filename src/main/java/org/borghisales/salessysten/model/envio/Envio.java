package org.borghisales.salessysten.model.envio;

public abstract class Envio {
    protected double costoBase;

    public Envio(double costoBase) {
        this.costoBase = costoBase;
    }

    public abstract double calcularCosto();
}