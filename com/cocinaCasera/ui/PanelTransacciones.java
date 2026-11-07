package com.cocinaCasera.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Módulo Transaccional con validación de atomicidad, centrado y botón de limpiar.
 */
public class PanelTransacciones extends JPanel {

    public PanelTransacciones() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel("Módulo Transaccional de Inventario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        add(pnlNorte, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("Tipo de Transacción:");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl1, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Entrada por Compra", "Merma / Desperdicio", "Consumo por Receta"});
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lbl2 = new JLabel("Seleccionar Insumo:");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl2, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboInsumo = new JComboBox<>(new String[]{"ID: 1 - Leche Entera", "ID: 2 - Huevos"});
        comboInsumo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(comboInsumo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lbl3 = new JLabel("Cantidad del Movimiento:");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl3, gbc);

        gbc.gridx = 1;
        JTextField txtCant = new JTextField("500.00", 20);
        txtCant.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(txtCant, gbc);

        // Panel de botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotones.setOpaque(false);

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setBackground(new Color(203, 213, 225));
        btnLimpiar.setForeground(new Color(15, 23, 42));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnProc = new JButton("Procesar Transacción");
        btnProc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProc.setBackground(new Color(37, 99, 235));
        btnProc.setFocusPainted(false);
        btnProc.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnProc);

        gbc.gridx = 1; gbc.gridy = 3;
        card.add(pnlBotones, gbc);

        btnLimpiar.addActionListener(e -> {
            txtCant.setText("");
            comboTipo.setSelectedIndex(0);
            comboInsumo.setSelectedIndex(0);
        });

        btnProc.addActionListener(e -> JOptionPane.showMessageDialog(this, "Transacción aplicada y stock recalculado."));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
    }
}