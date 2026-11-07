package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Réplica del Dashboard con título grande y tabla con badges "Vigente" / "Caducado".
 */
public class PanelDashboard extends JPanel {

    public PanelDashboard() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249)); // Fondo claro HTML
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // --- TÍTULO GRANDE CON LÍNEA DECORATIVA ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Dashboard e Inventario de Stock");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // --- TARJETA CONTENEDORA (CARD) ---
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)), // Borde izquierdo azul
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSub = new JLabel("Estado actual de insumos vigentes y cálculo automático de recetas (Ley del Mínimo de Liebig).");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(71, 85, 105));
        card.add(lblSub, BorderLayout.NORTH);

        // Tabla estilizada
        String[] columnas = {"ID Stock", "Ingrediente", "Existencia", "Fecha Caducidad", "Estado"};
        Object[][] datos = {
            {1, "Leche Entera", "1500.00 ml", "2026-11-20", "Vigente"},
            {2, "Huevos", "12.00 pz", "2026-11-12", "Vigente"},
            {3, "Harina de Trigo", "850.00 g", "2026-12-01", "Vigente"},
            {4, "Mantequilla", "200.00 g", "2026-11-05", "Caducado (Excluido)"}
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

        // Renderizador para pintar de color las celdas de "Estado"
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isS, boolean hasF, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, isS, hasF, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String estado = (String) val;
                if ("Vigente".equals(estado)) {
                    l.setBackground(new Color(220, 252, 231)); // Verde suave HTML
                    l.setForeground(new Color(22, 101, 52));
                } else {
                    l.setBackground(new Color(254, 226, 226)); // Rojo suave HTML
                    l.setForeground(new Color(153, 27, 27));
                }
                l.setOpaque(true);
                return l;
            }
        });

        card.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }
}