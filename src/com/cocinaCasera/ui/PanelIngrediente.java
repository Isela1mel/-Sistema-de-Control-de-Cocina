package com.cocinaCasera.ui;

import com.cocinaCasera.dao.IngredienteDAO;
import com.cocinaCasera.dao.InventarioDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.cocinaCasera.dao.IngredienteDAO.IngredienteFila;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/** Registro y consulta de ingredientes almacenados en MySQL. */
public class PanelIngrediente extends JPanel {
    private final JTextField txtNombre = new JTextField("", 20);
    private final JComboBox<UnidadOpcion> comboUnidad = new JComboBox<>(new UnidadOpcion[]{
        new UnidadOpcion("Gramos (g)", "g"), new UnidadOpcion("Mililitros (ml)", "ml"),
        new UnidadOpcion("Piezas (pz)", "pz"), new UnidadOpcion("Kilogramos (kg)", "kg"),
        new UnidadOpcion("Litros (l)", "l")
    });
    private final JTextField txtCaducidad = new JTextField("180", 20);
    private final JTextField txtNotas = new JTextField("", 20);
    private final JTextField txtExistencia = new JTextField("0", 20);
    private final DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"ID", "Ingrediente", "Unidad", "Días de vida útil", "Origen", "Notas"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    private final JTable tabla = new JTable(modelo);
    private final IngredienteDAO dao = new IngredienteDAO();

    public PanelIngrediente() {
        setLayout(new BorderLayout(18, 18));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));

        JLabel titulo = new JLabel("Gestión de Ingredientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(new Color(15, 23, 42));
        add(titulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);
        JPanel formulario = crearFormulario();
        contenido.add(formulario, BorderLayout.WEST);

        JPanel panelTabla = new JPanel(new BorderLayout(8, 8));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        JLabel subtitulo = new JLabel("Ingredientes registrados en la base de datos");
        subtitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTabla.add(subtitulo, BorderLayout.NORTH);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar lista");
        btnActualizar.addActionListener(e -> cargarIngredientes());
        JPanel pieTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pieTabla.setOpaque(false); pieTabla.add(btnActualizar);
        panelTabla.add(pieTabla, BorderLayout.SOUTH);
        contenido.add(panelTabla, BorderLayout.CENTER);
        add(contenido, BorderLayout.CENTER);
        cargarIngredientes();
    }

    private JPanel crearFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(330, 390));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(37, 99, 235)),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 5, 8, 5); g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        JLabel encabezado = new JLabel("Registrar ingrediente");
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 17)); card.add(encabezado, g);
        g.gridwidth = 1;
        agregarCampo(card, g, 1, "Nombre:", txtNombre);
        agregarCampo(card, g, 2, "Unidad contable:", comboUnidad);
        agregarCampo(card, g, 3, "Días de vida útil:", txtCaducidad);
        agregarCampo(card, g, 4, "Existencia inicial:", txtExistencia);
        agregarCampo(card, g, 5, "Notas:", txtNotas);

        JButton limpiar = new JButton("Limpiar");
        limpiar.addActionListener(e -> limpiarFormulario());
        JButton guardar = new JButton("Guardar ingrediente");
        guardar.setBackground(new Color(37, 99, 235)); guardar.setForeground(Color.BLACK);
        guardar.addActionListener(e -> guardarIngrediente());
        g.gridx = 0; g.gridy = 6; card.add(limpiar, g);
        g.gridx = 1; card.add(guardar, g);
        return card;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints g, int fila, String etiqueta, JComponent campo) {
        g.gridx = 0; g.gridy = fila; panel.add(new JLabel(etiqueta), g);
        g.gridx = 1; panel.add(campo, g);
    }

    private void guardarIngrediente() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del ingrediente.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus(); return;
        }
        Integer dias = null;
        String valorDias = txtCaducidad.getText().trim();
        if (!valorDias.isEmpty()) {
            try { dias = Integer.valueOf(valorDias); if (dias <= 0) throw new NumberFormatException(); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Los días de vida útil deben ser un número entero mayor que cero o quedar vacíos.");
                return;
            }
        }
        UnidadOpcion unidad = (UnidadOpcion) comboUnidad.getSelectedItem();
        try {
            BigDecimal existencia = new BigDecimal(txtExistencia.getText().trim().replace(",", "."));
            if (existencia.signum() < 0) throw new NumberFormatException();
            LocalDate caducidad = dias == null ? null : LocalDate.now().plusDays(dias);
            new InventarioDAO().altaIngrediente(nombre, unidad.codigo, dias, txtNotas.getText().trim(), existencia, caducidad);
            JOptionPane.showMessageDialog(this, "Ingrediente guardado correctamente en MySQL.");
            limpiarFormulario();
            cargarIngredientes();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La existencia inicial debe ser un número igual o mayor que cero.", "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            String mensaje = ex.getMessage();
            if (mensaje != null && mensaje.contains("Duplicate entry")) mensaje = "Ya existe un ingrediente con ese nombre.";
            JOptionPane.showMessageDialog(this, "No se pudo guardar el ingrediente:\n" + mensaje, "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarIngredientes() {
        try {
            modelo.setRowCount(0);
            for (IngredienteFila fila : dao.listarActivos()) {
                modelo.addRow(new Object[]{fila.id, fila.nombre, fila.unidad,
                    fila.diasVida == null ? "No especificado" : fila.diasVida, fila.origen, fila.notas == null ? "" : fila.notas});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la lista de ingredientes. Verifica que MySQL esté iniciado y que ConexionBD tenga los datos correctos.\n\n" + ex.getMessage(),
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtCaducidad.setText("180"); txtExistencia.setText("0"); txtNotas.setText(""); comboUnidad.setSelectedIndex(0); txtNombre.requestFocus();
    }

    private static class UnidadOpcion {
        final String etiqueta; final String codigo;
        UnidadOpcion(String etiqueta, String codigo) { this.etiqueta = etiqueta; this.codigo = codigo; }
        @Override public String toString() { return etiqueta; }
    }
    
    // Compatibilidad con el Dashboard para modificar datos de inventario.
    public static class DatosIngrediente {
        public String nombre;
        public String unidad;
        public String cantidad;
        public String fechaCaducidad;
        public String notas;

        public DatosIngrediente(String nombre, String unidad,
                String cantidad, String fechaCaducidad, String notas) {
            this.nombre = nombre;
            this.unidad = unidad;
            this.cantidad = cantidad;
            this.fechaCaducidad = fechaCaducidad;
            this.notas = notas;
        }
    }

    public static DatosIngrediente mostrarDialogoEdicion(
            java.awt.Component padre,
            String nombre,
            String unidad,
            String cantidad,
            String fechaCaducidad,
            String notas) {

        JTextField txtNombre = new JTextField(nombre, 15);
        JTextField txtUnidad = new JTextField(unidad, 15);
        JTextField txtCantidad = new JTextField(cantidad, 15);
        JTextField txtFecha = new JTextField(fechaCaducidad, 15);
        JTextField txtNotas = new JTextField(notas, 15);

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        panel.add(new JLabel("Ingrediente:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Unidad:"));
        panel.add(txtUnidad);

        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);

        panel.add(new JLabel("Fecha de caducidad:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Notas:"));
        panel.add(txtNotas);

        int resultado = JOptionPane.showConfirmDialog(
                padre,
                panel,
                "Modificar ingrediente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        return new DatosIngrediente(
                txtNombre.getText(),
                txtUnidad.getText(),
                txtCantidad.getText(),
                txtFecha.getText(),
                txtNotas.getText()
        );
    }

}
