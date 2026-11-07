package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Planificador de Compras automatizado según insumos faltantes.
 */
public class PanelPlanificador extends JPanel {

    public PanelPlanificador() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel("Planificador de Adquisiciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        add(pnlNorte, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSub = new JLabel("Lista automática de compras generada según el inventario disponible y recetas seleccionadas.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(71, 85, 105));
        card.add(lblSub, BorderLayout.NORTH);

        String[] columnas = {"Ingrediente Faltante", "Cantidad Necesaria a Comprar", "Unidad Contable"};
        Object[][] datos = {
            {"Azúcar Morena", "500.00", "Gramos (g)"},
            {"Esencia de Vainilla", "15.00", "Mililitros (ml)"}
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

        JButton btnExportar = new JButton("Exportar Lista de Compras (Adquisición)");
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExportar.setBackground(new Color(37, 99, 235));
        btnExportar.setFocusPainted(false);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnExportar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Lista de adquisiciones exportada con éxito."));
        
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.setOpaque(false);
        pnlSur.add(btnExportar);
        card.add(pnlSur, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }
}