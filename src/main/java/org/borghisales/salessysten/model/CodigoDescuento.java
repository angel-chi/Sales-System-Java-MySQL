package org.borghisales.salessysten.model;

import java.time.LocalDate;

public class CodigoDescuento {
    private int id;
    private String codigo;
    private double porcentaje;
    private Estado estado;
    private LocalDate fechaExpiracion;

    public enum Estado {
        ACTIVO, INACTIVO
    }

    public CodigoDescuento(int id, String codigo, double porcentaje, Estado estado, LocalDate fechaExpiracion) {
        this.id = id;
        this.codigo = codigo;
        this.porcentaje = porcentaje;
        this.estado = estado;
        this.fechaExpiracion = fechaExpiracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
