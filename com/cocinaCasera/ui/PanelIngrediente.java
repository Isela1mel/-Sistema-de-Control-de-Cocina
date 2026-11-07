package com.cocinaCasera.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario 1: Registro de Nuevos Ingredientes con centrado y botón de limpiar.
 */
public class PanelIngrediente extends JPanel {

    public PanelIngrediente() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título Grande
        JLabel lblTitulo = new JLabel("Formulario: Registro de Nuevo Ingrediente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));
        
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        add(pnlNorte, BorderLayout.NORTH);

        // Tarjeta de Formulario
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
        JLabel lbl1 = new JLabel("Nombre Ingrediente:");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl1, gbc);

        gbc.gridx = 1;
        JTextField txtNombre = new JTextField("Ej. Azúcar Morena", 20);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lbl2 = new JLabel("Unidad Contable:");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl2, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboUnidad = new JComboBox<>(new String[]{"Gramos (g)", "Mililitros (ml)", "Piezas (pz)", "Kilogramos (kg)"});
        comboUnidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(comboUnidad, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lbl3 = new JLabel("Días Caducidad Mín.:");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lbl3, gbc);

        gbc.gridx = 1;
        JTextField txtCaducidad = new JTextField("180", 20);
        txtCaducidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        card.add(txtCaducidad, gbc);

        // Panel de botones (Guardar + Limpiar)
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotones.setOpaque(false);

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setBackground(new Color(203, 213, 225));
        btnLimpiar.setForeground(new Color(15, 23, 42));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnGuardar = new JButton("Guardar Ingrediente");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(37, 99, 235));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnGuardar);

        gbc.gridx = 1; gbc.gridy = 3;
        card.add(pnlBotones, gbc);

        // Acciones de los botones
        btnLimpiar.addActionListener(e -> {
            txtNombre.setText("");
            txtCaducidad.setText("");
            comboUnidad.setSelectedIndex(0);
        });

        btnGuardar.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡Ingrediente guardado con éxito!"));

        // Envoltorio con GridBagLayout para centrar perfectamente la tarjeta en la pantalla
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
    }
}