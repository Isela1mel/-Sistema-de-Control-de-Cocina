package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Módulo para consultar el catálogo de recetas registradas,
 * sus imágenes ilustrativas y los ingredientes consumidos.
 */
public class PanelListaRecetas extends JPanel {

    public PanelListaRecetas() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título Grande con línea decorativa
        JLabel lblTitulo = new JLabel("Catálogo de Recetas y Platillos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        add(pnlNorte, BorderLayout.NORTH);

        // Tarjeta contenedora
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSub = new JLabel("Listado de platillos configurados y su disponibilidad actual basada en stock.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(71, 85, 105));
        card.add(lblSub, BorderLayout.NORTH);

        // Tabla de recetas
        String[] columnas = {"ID Receta", "Nombre del Platillo", "Ingredientes Principales", "Porciones Máx. Posibles"};
        Object[][] datos = {
            {1, "Hot Cakes Tradicionales", "Leche, Huevos, Harina, Mantequilla", "3 porciones"},
            {2, "Omelette de Huevos", "Huevos, Mantequilla", "6 porciones"},
            {3, "Licuado de Plátano", "Leche Entera", "3 porciones"}
        };

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(248, 250, 252));

        card.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón inferior para ver detalles
        JButton btnVerDetalle = new JButton("Ver Detalle / Ingredientes de la Receta");
        btnVerDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerDetalle.setBackground(new Color(15, 23, 42));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVerDetalle.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mostrando desglose de insumos y cantidades requeridas para el platillo seleccionado."));

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.setOpaque(false);
        pnlSur.add(btnVerDetalle);
        card.add(pnlSur, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }
}