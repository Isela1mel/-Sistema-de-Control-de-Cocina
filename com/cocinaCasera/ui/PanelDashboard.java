package com.cocinaCasera.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
        String[] columnas = {"ID Stock", "Ingrediente", "Existencia", "Fecha Caducidad", "Estado", "Notas", "Modificar", "Eliminar"};
        Object[][] datos = {
            {1, "Leche Entera", "1500.00 ml", "2026-11-20", "Vigente", "", "Modificar", "Eliminar"},
            {2, "Huevos", "12.00 pz", "2026-11-12", "Vigente", "", "Modificar", "Eliminar"},
            {3, "Harina de Trigo", "850.00 g", "2026-12-01", "Vigente", "", "Modificar", "Eliminar"},
            {4, "Mantequilla", "200.00 g", "2026-11-05", "Caducado (Excluido)", "", "Modificar", "Eliminar"}
        };

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 5 || c == 6 || c == 7; }
        };

        JTable tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(248, 250, 252));
        tabla.getColumnModel().getColumn(5).setPreferredWidth(180);

        configurarBotonColumna(tabla, 6, "Modificar", fila -> {
            String existenciaActual = String.valueOf(model.getValueAt(fila, 2));
            String cantidadInicial = existenciaActual.replaceAll("[^0-9.].*", "");
            String unidad = existenciaActual.replaceFirst("^[0-9.]+\\s*", "");
            PanelIngrediente.DatosIngrediente datosEdicion = PanelIngrediente.mostrarDialogoEdicion(
                this,
                String.valueOf(model.getValueAt(fila, 1)),
                unidad,
                cantidadInicial,
                String.valueOf(model.getValueAt(fila, 3)),
                String.valueOf(model.getValueAt(fila, 5))
            );
            if (datosEdicion == null) return;
            try {
                if (datosEdicion.nombre.trim().isEmpty() || datosEdicion.cantidad.trim().isEmpty()) throw new IllegalArgumentException();
                double cantidad = Double.parseDouble(datosEdicion.cantidad.trim());
                if (!Double.isFinite(cantidad) || cantidad < 0) throw new NumberFormatException();
                LocalDate caducidad = LocalDate.parse(datosEdicion.fechaCaducidad.trim());
                model.setValueAt(datosEdicion.nombre.trim(), fila, 1);
                model.setValueAt(String.format(java.util.Locale.US, "%.2f %s", cantidad, datosEdicion.unidad), fila, 2);
                model.setValueAt(caducidad.toString(), fila, 3);
                model.setValueAt(caducidad.isBefore(LocalDate.now()) ? "Caducado (Excluido)" : "Vigente", fila, 4);
                model.setValueAt(datosEdicion.notas.trim(), fila, 5);
            } catch (IllegalArgumentException | DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Verifique el nombre, la cantidad y la fecha (AAAA-MM-DD).", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            }
        });
        configurarBotonColumna(tabla, 7, "Eliminar", fila -> {
            int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el ingrediente " + model.getValueAt(fila, 1) + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) model.removeRow(fila);
        });

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

    private void configurarBotonColumna(JTable tabla, int columna, String texto, AccionFila accion) {
        Color fondo = "Eliminar".equals(texto) ? new Color(220, 38, 38) : new Color(37, 99, 235);
        Color textoColor = Color.WHITE;
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new RenderizadorBoton(texto, fondo, textoColor));
        tabla.getColumnModel().getColumn(columna).setCellEditor(new EditorBoton(tabla, texto, accion, fondo, textoColor));
        tabla.getColumnModel().getColumn(columna).setPreferredWidth(95);
    }

    @FunctionalInterface
    private interface AccionFila {
        void ejecutar(int filaModelo);
    }

    private static class RenderizadorBoton extends JButton implements TableCellRenderer {
        RenderizadorBoton(String texto, Color fondo, Color colorTexto) {
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

        EditorBoton(JTable tabla, String texto, AccionFila accion, Color fondo, Color colorTexto) {
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