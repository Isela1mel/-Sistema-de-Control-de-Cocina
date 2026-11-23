package com.cocinaCasera.ui;

import com.cocinaCasera.dao.InventarioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.SQLException;

/** Catálogo de recetas guardadas realmente en MySQL. */
public class PanelListaRecetas extends JPanel {
    private final DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"ID", "Receta", "Porciones base", "Ingredientes", "Instrucciones"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public PanelListaRecetas() {
        setLayout(new BorderLayout(18, 18));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        JLabel titulo = new JLabel("Catálogo de Recetas y Platillos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        add(titulo, BorderLayout.NORTH);

        tabla.setRowHeight(42);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getColumnModel().getColumn(0).setPreferredWidth(55);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(190);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(105);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(270);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(420);
        TableCellRenderer renderer = new MultiLineRenderer();
        tabla.getColumnModel().getColumn(3).setCellRenderer(renderer);
        tabla.getColumnModel().getColumn(4).setCellRenderer(renderer);
        tabla.setToolTipText("Los ingredientes y sus cantidades se obtienen de la relación receta_ingrediente.");

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        card.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton actualizar = new JButton("Actualizar recetas");
        actualizar.addActionListener(e -> actualizar());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(actualizar);
        card.add(sur, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
        actualizar();
    }

    public final void actualizar() {
        try {
            modelo.setRowCount(0);
            for (Object[] fila : new InventarioDAO().listaRecetas()) modelo.addRow(fila);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudieron consultar las recetas: " + e.getMessage(), "MySQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class MultiLineRenderer extends JTextArea implements TableCellRenderer {
        MultiLineRenderer() { setLineWrap(true); setWrapStyleWord(true); setOpaque(true); setFont(new Font("Segoe UI", Font.PLAIN, 13)); }
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setBackground(selected ? table.getSelectionBackground() : table.getBackground());
            setForeground(selected ? table.getSelectionForeground() : table.getForeground());
            int width = table.getColumnModel().getColumn(column).getWidth();
            setSize(width, Short.MAX_VALUE);
            int height = Math.max(42, getPreferredSize().height + 8);
            if (table.getRowHeight(row) != height) table.setRowHeight(row, height);
            return this;
        }
    }
}
