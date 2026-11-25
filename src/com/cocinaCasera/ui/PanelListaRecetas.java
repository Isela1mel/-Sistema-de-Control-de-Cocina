
package com.cocinaCasera.ui;

import com.cocinaCasera.dao.InventarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.io.File;
import java.net.URL;

import java.sql.SQLException;

/**
 * Catálogo de recetas guardadas en MySQL.
 * Muestra una miniatura de la imagen de cada receta.
 */
public class PanelListaRecetas extends JPanel {

    private final DefaultTableModel modelo =
        new DefaultTableModel(
            new Object[]{
                "ID",
                "Imagen",
                "Receta",
                "Porciones base",
                "Ingredientes",
                "Instrucciones"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

    private final JTable tabla = new JTable(modelo);

    public PanelListaRecetas() {

        setLayout(new BorderLayout(18, 18));
        setBackground(new Color(241, 245, 249));

        setBorder(
            BorderFactory.createEmptyBorder(
                25, 30, 25, 30
            )
        );

        JLabel titulo =
            new JLabel("Catálogo de Recetas y Platillos");

        titulo.setFont(
            new Font("Segoe UI", Font.BOLD, 25)
        );

        add(titulo, BorderLayout.NORTH);

        tabla.setRowHeight(90);

        tabla.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        tabla.getTableHeader()
             .setReorderingAllowed(false);

        tabla.setFont(
            new Font("Segoe UI", Font.PLAIN, 13)
        );

        // Ancho de las columnas
        tabla.getColumnModel().getColumn(0)
             .setPreferredWidth(55);

        tabla.getColumnModel().getColumn(1)
             .setPreferredWidth(110);

        tabla.getColumnModel().getColumn(2)
             .setPreferredWidth(190);

        tabla.getColumnModel().getColumn(3)
             .setPreferredWidth(105);

        tabla.getColumnModel().getColumn(4)
             .setPreferredWidth(270);

        tabla.getColumnModel().getColumn(5)
             .setPreferredWidth(420);

        // Renderizador de imágenes
        tabla.getColumnModel().getColumn(1)
             .setCellRenderer(new ImagenRenderer());

        // Renderizador de texto multilínea
        TableCellRenderer renderer =
            new MultiLineRenderer();

        tabla.getColumnModel().getColumn(4)
             .setCellRenderer(renderer);

        tabla.getColumnModel().getColumn(5)
             .setCellRenderer(renderer);

        tabla.setToolTipText(
            "Las imágenes se cargan desde los recursos del proyecto."
        );

        JPanel card =
            new JPanel(new BorderLayout(10, 10));

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createEmptyBorder(
                18, 18, 18, 18
            )
        );

        card.add(
            new JScrollPane(tabla),
            BorderLayout.CENTER
        );

        JButton actualizar =
            new JButton("Actualizar recetas");

        actualizar.addActionListener(
            e -> actualizar()
        );

        JPanel sur =
            new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
            );

        sur.add(actualizar);

        card.add(sur, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        actualizar();
    }

    /**
     * Consulta nuevamente las recetas en MySQL.
     */
    public final void actualizar() {

        try {

            modelo.setRowCount(0);

            for (Object[] fila :
                    new InventarioDAO().listaRecetas()) {

                modelo.addRow(fila);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "No se pudieron consultar las recetas: "
                    + e.getMessage(),
                "MySQL",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Renderizador para mostrar imágenes en la tabla.
     */
    private static class ImagenRenderer
            extends JLabel
            implements TableCellRenderer {

        private static final int ANCHO = 90;
        private static final int ALTO = 70;

        ImagenRenderer() {

            setHorizontalAlignment(
                SwingConstants.CENTER
            );

            setVerticalAlignment(
                SwingConstants.CENTER
            );

            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column) {

            setIcon(null);
            setText("");

            setBackground(
                selected
                    ? table.getSelectionBackground()
                    : Color.WHITE
            );

            setForeground(
                selected
                    ? table.getSelectionForeground()
                    : table.getForeground()
            );

            if (value != null) {

                String ruta = value.toString();

                try {

                    ImageIcon original =
                        cargarImagen(ruta);

                    if (original != null
                            && original.getIconWidth() > 0
                            && original.getIconHeight() > 0) {

                        Image imagen =
                            original.getImage();

                        // Redimensionar manteniendo proporción
                        int anchoOriginal =
                            original.getIconWidth();

                        int altoOriginal =
                            original.getIconHeight();

                        double escala = Math.min(
                            (double) ANCHO / anchoOriginal,
                            (double) ALTO / altoOriginal
                        );

                        int nuevoAncho =
                            Math.max(
                                1,
                                (int) (anchoOriginal * escala)
                            );

                        int nuevoAlto =
                            Math.max(
                                1,
                                (int) (altoOriginal * escala)
                            );

                        Image miniatura =
                            imagen.getScaledInstance(
                                nuevoAncho,
                                nuevoAlto,
                                Image.SCALE_SMOOTH
                            );

                        setIcon(new ImageIcon(miniatura));
                    }

                } catch (Exception e) {

                    // Si falla la carga, la celda queda vacía.
                    setIcon(null);
                    setText("");
                }
            }

            return this;
        }

        /**
         * Busca la imagen primero en los recursos
         * del proyecto y después en el sistema de archivos.
         */
        private ImageIcon cargarImagen(String ruta) {

            if (ruta == null || ruta.isBlank()) {
                return null;
            }

            try {

                // 1. Buscar como recurso del proyecto.
                String recurso = ruta.startsWith("/")
                    ? ruta
                    : "/" + ruta;

                URL url =
                    getClass().getResource(recurso);

                if (url != null) {
                    return new ImageIcon(url);
                }

                // 2. Buscar como archivo local.
                String rutaArchivo =
                    ruta.startsWith("/")
                        ? ruta.substring(1)
                        : ruta;

                File archivo =
                    new File(rutaArchivo);

                if (archivo.exists()
                        && archivo.isFile()) {

                    return new ImageIcon(
                        archivo.getAbsolutePath()
                    );
                }

                // 3. Si no existe, no mostrar nada.
                return null;

            } catch (Exception e) {

                return null;
            }
        }
    }

    /**
     * Renderizador para ingredientes e instrucciones.
     */
    private static class MultiLineRenderer
            extends JTextArea
            implements TableCellRenderer {

        MultiLineRenderer() {

            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);

            setFont(
                new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    13
                )
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column) {

            setText(
                value == null
                    ? ""
                    : value.toString()
            );

            setBackground(
                selected
                    ? table.getSelectionBackground()
                    : table.getBackground()
            );

            setForeground(
                selected
                    ? table.getSelectionForeground()
                    : table.getForeground()
            );

            int width =
                table.getColumnModel()
                     .getColumn(column)
                     .getWidth();

            setSize(width, Short.MAX_VALUE);

            int height = Math.max(
                90,
                getPreferredSize().height + 8
            );

            if (table.getRowHeight(row) != height) {
                table.setRowHeight(row, height);
            }

            return this;
        }
    }
}
