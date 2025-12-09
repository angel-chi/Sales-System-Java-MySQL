package org.borghisales.salessysten.model.entities;

public enum State {
    ACTIVE("ACTIVO"),
    DISACTIVE("INACTIVO");

    private final String displayName;

    State(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getEnglishName() {
        return this.name();
    }
}