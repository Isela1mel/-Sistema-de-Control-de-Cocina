package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Formulario 2: Alta de Recetas con múltiples ingredientes, pasos y botón de limpiar.
 */
public class PanelReceta extends JPanel {

    private DefaultTableModel modeloTablaIngredientes;
    private JTextField txtNombreReceta;
    private JTextField txtCantidadReq;
    private JTextArea txtPasos;

    public PanelReceta() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTitulo = new JLabel("Formulario: Alta de Receta y Múltiples Insumos");
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

        // Panel Superior: Nombre y Selección de Insumos
        JPanel pnlDatos = new JPanel(new GridBagLayout());
        pnlDatos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("Nombre Receta:");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlDatos.add(lbl1, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNombreReceta = new JTextField("Ej. Hot Cakes Tradicionales", 25);
        txtNombreReceta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlDatos.add(txtNombreReceta, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lbl2 = new JLabel("Seleccionar Insumo:");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlDatos.add(lbl2, gbc);

        gbc.gridx = 1;
        JComboBox<String> comboIngredientes = new JComboBox<>(new String[]{
            "1 - Leche Entera (ml)", 
            "2 - Huevos (pz)", 
            "3 - Harina de Trigo (g)", 
            "4 - Mantequilla (g)"
        });
        comboIngredientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlDatos.add(comboIngredientes, gbc);

        gbc.gridx = 2;
        JLabel lbl3 = new JLabel("Cant. Requerida:");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlDatos.add(lbl3, gbc);

        gbc.gridx = 3;
        txtCantidadReq = new JTextField("250", 8);
        txtCantidadReq.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlDatos.add(txtCantidadReq, gbc);

        JButton btnAddIngrediente = new JButton("➕ Añadir a la Receta");
        btnAddIngrediente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddIngrediente.setBackground(new Color(22, 163, 74));
        btnAddIngrediente.setForeground(Color.WHITE);
        btnAddIngrediente.setFocusPainted(false);
        btnAddIngrediente.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 3; gbc.gridy = 2;
        pnlDatos.add(btnAddIngrediente, gbc);

        // Panel Central: Tabla y Pasos
        JPanel pnlCentroSplit = new JPanel(new GridLayout(2, 1, 10, 10));
        pnlCentroSplit.setOpaque(false);

        String[] columnasTabla = {"ID Insumo", "Ingrediente", "Cantidad Requerida"};
        modeloTablaIngredientes = new DefaultTableModel(columnasTabla, 0);
        JTable tablaIngs = new JTable(modeloTablaIngredientes);
        tablaIngs.setRowHeight(26);

        JScrollPane scrollTabla = new JScrollPane(tablaIngs);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Ingredientes Asociados a la Receta"));

        txtPasos = new JTextArea("1. Mezclar ingredientes.\n2. Cocinar en sartén.", 3, 20);
        txtPasos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPasos = new JScrollPane(txtPasos);
        scrollPasos.setBorder(BorderFactory.createTitledBorder("Pasos de Preparación / Instrucciones del Platillo"));

        pnlCentroSplit.add(scrollTabla);
        pnlCentroSplit.add(scrollPasos);

        JPanel pnlContenedorForm = new JPanel(new BorderLayout(10, 10));
        pnlContenedorForm.setOpaque(false);
        pnlContenedorForm.add(pnlDatos, BorderLayout.NORTH);
        pnlContenedorForm.add(pnlCentroSplit, BorderLayout.CENTER);

        card.add(pnlContenedorForm, BorderLayout.CENTER);

        // Panel inferior con botones (Limpiar y Guardar)
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlSur.setOpaque(false);

        JButton btnLimpiarReceta = new JButton("Limpiar Todo");
        btnLimpiarReceta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiarReceta.setBackground(new Color(203, 213, 225));
        btnLimpiarReceta.setForeground(new Color(15, 23, 42));
        btnLimpiarReceta.setFocusPainted(false);
        btnLimpiarReceta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnGuardarReceta = new JButton("Registrar Receta Completa");
        btnGuardarReceta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardarReceta.setBackground(new Color(37, 99, 235));
        btnGuardarReceta.setFocusPainted(false);
        btnGuardarReceta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlSur.add(btnLimpiarReceta);
        pnlSur.add(btnGuardarReceta);
        card.add(pnlSur, BorderLayout.SOUTH);

        // Eventos
        btnAddIngrediente.addActionListener(e -> {
            String insumoSeleccionado = (String) comboIngredientes.getSelectedItem();
            String cantidad = txtCantidadReq.getText().trim();
            if(!cantidad.isEmpty()) {
                modeloTablaIngredientes.addRow(new Object[]{
                    insumoSeleccionado.split(" - ")[0], 
                    insumoSeleccionado.split(" - ")[1], 
                    cantidad
                });
                txtCantidadReq.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese la cantidad requerida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLimpiarReceta.addActionListener(e -> {
            txtNombreReceta.setText("");
            txtCantidadReq.setText("");
            txtPasos.setText("");
            modeloTablaIngredientes.setRowCount(0); // Vaciar tabla
        });

        btnGuardarReceta.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡Receta registrada con éxito!"));

        add(card, BorderLayout.CENTER);
    }
}