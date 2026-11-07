package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Módulo para consultar el historial de transacciones registradas
 * (entradas por compra, mermas/desperdicios y consumos por recetas).
 */
public class PanelHistorialTransaccion extends JPanel {

    public PanelHistorialTransaccion() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título Grande con línea decorativa
        JLabel lblTitulo = new JLabel("Historial de Transacciones y Movimientos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        add(pnlNorte, BorderLayout.NORTH);

        // Tarjeta contenedora principal
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblSub = new JLabel("Registro detallado de entradas de stock, mermas por caducidad y consumos automáticos.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(71, 85, 105));
        card.add(lblSub, BorderLayout.NORTH);

        // Tabla de Historial
        String[] columnas = {"ID Transacción", "Tipo de Movimiento", "Insumo / Platillo", "Cantidad", "Fecha y Hora"};
        Object[][] datos = {
            {101, "Entrada por Compra", "Leche Entera", "+2000.00 ml", "2026-11-01 10:15"},
            {102, "Merma / Desperdicio", "Mantequilla", "-200.00 g", "2026-11-02 08:30"},
            {103, "Consumo por Receta", "Hot Cakes Tradicionales", "-3 porciones", "2026-11-03 14:20"}
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

        // Botón inferior para exportar o filtrar
        JButton btnFiltrar = new JButton("Filtrar por Fecha o Tipo");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFiltrar.setBackground(new Color(37, 99, 235));
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnFiltrar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abriendo opciones de filtrado del historial de transacciones."));

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.setOpaque(false);
        pnlSur.add(btnFiltrar);
        card.add(pnlSur, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
    }
}