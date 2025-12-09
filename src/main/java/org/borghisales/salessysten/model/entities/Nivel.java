package org.borghisales.salessysten.model.entities;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

public enum Nivel {
    ADMIN("Administrador", true, true, true),
    JEFE("Jefe", true, true, true),
    CONTADOR("Contador",false, false, true),
    VENDEDOR("Vendedor", true, false, false);

    private final String label;
    private final boolean puedeVender;
    private final boolean editarPersonal;
    private final boolean revisarReportes;

    Nivel(String label, boolean puedeVender, boolean editarPersonal, boolean revisarReportes) {
        this.label = label;
        this.editarPersonal = editarPersonal;
        this.puedeVender = puedeVender;
        this.revisarReportes = revisarReportes;
    }

    public String getLabel() {
        return label;
    }

    public boolean puedeEditarPersona() {return this.editarPersonal;}
    public boolean puedeRevisarReportes() {return this.revisarReportes;}
    public boolean puedeVender() {return this.puedeVender;}
    public static Nivel fromString(String nivel){
        if (nivel == null) return null;
        String nivelAcceso = nivel.trim();
        try {return Nivel.valueOf(nivelAcceso.toUpperCase());}
        catch (IllegalArgumentException ignored) {MenuController.setAlert(Alert.AlertType.ERROR,"Cannot delete the current seller");}
        for (Nivel n : values()) { if (n.label.equalsIgnoreCase(nivelAcceso)) return n;}
        return null;
    }
}

