package org.borghisales.salessysten.model;

public abstract class Usuario {
    protected int id;
    protected String nombre;
    protected Estado estado;

    public enum Estado { ACTIVO, INACTIVO }

    public Usuario(int id, String nombre, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    //Métodos getter
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Estado getEstado() {
        return estado;
    }
}
