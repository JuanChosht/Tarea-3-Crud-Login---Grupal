package com.pagoseguro.model;

public enum EstadoTransaccion {
    CREADA("Creada", "text-bg-secondary"),
    PAGADA("Pagada · retenida", "text-bg-warning"),
    ENVIADA("Enviada", "text-bg-info"),
    LIBERADA("Liberada al vendedor", "text-bg-success"),
    EN_DISPUTA("En disputa", "text-bg-danger"),
    REEMBOLSADA("Reembolsada", "text-bg-dark"),
    CANCELADA("Cancelada", "text-bg-light border");

    private final String etiqueta;
    private final String claseBadge;

    EstadoTransaccion(String etiqueta, String claseBadge) {
        this.etiqueta = etiqueta;
        this.claseBadge = claseBadge;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getClaseBadge() {
        return claseBadge;
    }

    public boolean esRetenido() {
        return this == PAGADA || this == ENVIADA || this == EN_DISPUTA;
    }
}
