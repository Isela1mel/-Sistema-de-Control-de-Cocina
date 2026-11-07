package com.cocinaCasera.model;

/**
 * Clase que representa un Insumo o Ingrediente en el sistema.
 * Basado en la entidad INGREDIENTE de la especificación técnica.
 */
public class Ingrediente {
    private int idIngrediente;      // Identificador único (PK)
    private String nombre;          // Nombre común del ingrediente
    private String unidadMedida;    // Piezas, Gramos, Mililitros, etc.
    private int diasCaducidadMin;   // Días de vida útil predeterminados

    // Constructor vacío
    public Ingrediente() {}

    // Constructor con parámetros
    public Ingrediente(int idIngrediente, String nombre, String unidadMedida, int diasCaducidadMin) {
        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.diasCaducidadMin = diasCaducidadMin;
    }

    // Getters y Setters
    public int getIdIngrediente() { return idIngrediente; }
    public void setIdIngrediente(int idIngrediente) { this.idIngrediente = idIngrediente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public int getDiasCaducidadMin() { return diasCaducidadMin; }
    public void setDiasCaducidadMin(int diasCaducidadMin) { this.diasCaducidadMin = diasCaducidadMin; }

    @Override
    public String toString() {
        return idIngrediente + " - " + nombre;
    }
}
