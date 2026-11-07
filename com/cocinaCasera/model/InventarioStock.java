package com.cocinaCasera.model;

import java.time.LocalDate;

/**
 * Clase que controla los lotes de stock disponibles y sus fechas de caducidad.
 * Basado en la entidad INVENTARIO_STOCK.
 */
public class InventarioStock {
    private int idStock;            // Identificador del lote
    private int idIngrediente;      // Llave foránea hacia Ingrediente
    private String nombreIngrediente; // Nombre auxiliar para visualización en tablas
    private double existencia;      // Cantidad actual disponible
    private LocalDate fechaCaducidad; // Fecha límite de consumo

    public InventarioStock() {}

    public InventarioStock(int idStock, int idIngrediente, String nombreIngrediente, double existencia, LocalDate fechaCaducidad) {
        this.idStock = idStock;
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.existencia = existencia;
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Valida si el lote actual se encuentra vigente o ya caducado (RF-03).
     * @return true si es apto para consumo, false si está vencido.
     */
    public boolean estaVigente() {
        return LocalDate.now().isBefore(fechaCaducidad) || LocalDate.now().isEqual(fechaCaducidad);
    }

    // Getters y Setters
    public int getIdStock() { return idStock; }
    public void setIdStock(int idStock) { this.idStock = idStock; }

    public int getIdIngrediente() { return idIngrediente; }
    public void setIdIngrediente(int idIngrediente) { this.idIngrediente = idIngrediente; }

    public String getNombreIngrediente() { return nombreIngrediente; }
    public void setNombreIngrediente(String nombreIngrediente) { this.nombreIngrediente = nombreIngrediente; }

    public double getExistencia() { return existencia; }
    public void setExistencia(double existencia) { this.existencia = existencia; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}
