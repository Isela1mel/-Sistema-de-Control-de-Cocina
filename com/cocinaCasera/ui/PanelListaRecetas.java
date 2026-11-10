package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Módulo para consultar el catálogo de recetas registradas,
 * sus imágenes ilustrativas y los ingredientes consumidos.
 */
public class PanelListaRecetas extends JPanel {
    private JPanel panelVistas;
    private PanelReceta formularioEdicion;

    public PanelListaRecetas() {
        setLayout(new BorderLayout());
        panelVistas = new JPanel(new CardLayout());
        panelVistas.add(crearVistaLista(), "LISTA");
        add(panelVistas, BorderLayout.CENTER);
    }

    private JPanel crearVistaLista() {
        JPanel vistaLista = new JPanel(new BorderLayout(20, 20));
        vistaLista.setBackground(new Color(241, 245, 249));
        vistaLista.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título Grande con línea decorativa
        JLabel lblTitulo = new JLabel("Catálogo de Recetas y Platillos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(37, 99, 235)));

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.WEST);
        vistaLista.add(pnlNorte, BorderLayout.NORTH);

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
        String[] columnas = {"ID Receta", "Nombre del Platillo", "Ingredientes Principales", "Porciones Máx. Posibles", "Modificar", "Eliminar", "Detalle Ingredientes", "Pasos"};
        Object[][] datos = {
            {1, "Hot Cakes Tradicionales", "Leche, Huevos, Harina, Mantequilla", "3 porciones", "Modificar", "Eliminar", null, ""},
            {2, "Omelette de Huevos", "Huevos, Mantequilla", "6 porciones", "Modificar", "Eliminar", null, ""},
            {3, "Licuado de Plátano", "Leche Entera", "3 porciones", "Modificar", "Eliminar", null, ""}
        };

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4 || c == 5; }
        };

        JTable tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(248, 250, 252));
        tabla.removeColumn(tabla.getColumnModel().getColumn(7));
        tabla.removeColumn(tabla.getColumnModel().getColumn(6));

        configurarBotonColumna(tabla, 4, "Modificar", fila -> {
            abrirFormularioEdicion(model, fila);
        });
        configurarBotonColumna(tabla, 5, "Eliminar", fila -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la receta " + model.getValueAt(fila, 1) + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) model.removeRow(fila);
        });

        card.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón inferior para ver detalles
        JButton btnVerDetalle = new JButton("Ver Detalle / Ingredientes de la Receta");
        btnVerDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerDetalle.setBackground(new Color(15, 23, 42));
        btnVerDetalle.setFocusPainted(false);
        btnVerDetalle.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVerDetalle.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mostrando desglose de insumos y cantidades requeridas para el platillo seleccionado."));

        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.setOpaque(false);
        pnlSur.add(btnVerDetalle);
        card.add(pnlSur, BorderLayout.SOUTH);

        vistaLista.add(card, BorderLayout.CENTER);
        return vistaLista;
    }

    private void abrirFormularioEdicion(DefaultTableModel model, int fila) {
        if (formularioEdicion != null) panelVistas.remove(formularioEdicion);

        formularioEdicion = PanelReceta.crearFormularioEdicion(
            String.valueOf(model.getValueAt(fila, 1)),
            String.valueOf(model.getValueAt(fila, 2)),
            String.valueOf(model.getValueAt(fila, 3)),
            model.getValueAt(fila, 6),
            String.valueOf(model.getValueAt(fila, 7)),
            receta -> {
                model.setValueAt(receta.nombre, fila, 1);
                model.setValueAt(receta.ingredientes, fila, 2);
                model.setValueAt(receta.porciones, fila, 3);
                model.setValueAt(receta.detalleIngredientes, fila, 6);
                model.setValueAt(receta.pasos, fila, 7);
                model.fireTableDataChanged();
                mostrarLista();
            },
            this::mostrarLista
        );
        panelVistas.add(formularioEdicion, "EDICION");
        ((CardLayout) panelVistas.getLayout()).show(panelVistas, "EDICION");
    }

    private void mostrarLista() {
        ((CardLayout) panelVistas.getLayout()).show(panelVistas, "LISTA");
    }

    private void configurarBotonColumna(JTable tabla, int columna, String texto, AccionFila accion) {
        Color fondo = "Eliminar".equals(texto) ? new Color(220, 38, 38) : new Color(37, 99, 235);
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new RenderizadorBoton(texto, fondo));
        tabla.getColumnModel().getColumn(columna).setCellEditor(new EditorBoton(tabla, texto, accion, fondo));
        tabla.getColumnModel().getColumn(columna).setPreferredWidth(95);
    }

    @FunctionalInterface
    private interface AccionFila {
        void ejecutar(int filaModelo);
    }

    private static class RenderizadorBoton extends JButton implements TableCellRenderer {
        RenderizadorBoton(String texto, Color fondo) {
            setText(texto);
            setBackground(fondo);
            setOpaque(true);
            setContentAreaFilled(true);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado, boolean tieneFoco, int fila, int columna) {
            return this;
        }
    }

    private static class EditorBoton extends AbstractCellEditor implements TableCellEditor {
        private final JButton boton;
        private int filaModelo;

        EditorBoton(JTable tabla, String texto, AccionFila accion, Color fondo) {
            boton = new JButton(texto);
            boton.setBackground(fondo);
            boton.setOpaque(true);
            boton.setContentAreaFilled(true);
            boton.setFocusPainted(false);
            boton.addActionListener(e -> {
                fireEditingStopped();
                accion.ejecutar(filaModelo);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable tabla, Object valor, boolean seleccionado, int fila, int columna) {
            filaModelo = tabla.convertRowIndexToModel(fila);
            return boton;
        }

        @Override
        public Object getCellEditorValue() {
            return boton.getText();
        }
    }
}