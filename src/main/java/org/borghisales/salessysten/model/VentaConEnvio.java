package org.borghisales.salessysten.model;

import org.borghisales.salessysten.model.envio.Envio;

public class VentaConEnvio {
    private final Sales venta;
    private final Envio envio;

    public VentaConEnvio(Sales venta, Envio envio) {
        this.venta = venta;
        this.envio = envio;
    }

    public double calcularTotal() {
        return venta.total() + envio.calcularCosto();
    }
}
