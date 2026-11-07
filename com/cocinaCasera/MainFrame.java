package com.cocinaCasera;

import com.cocinaCasera.ui.*;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana Principal del Sistema de Control de Cocina Casera en Java.
 * Gestiona el menú lateral, el CardLayout y la navegación entre todos los módulos.
 */
public class MainFrame extends JFrame {

    private JPanel panelContenidoCentral;
    private JPanel panelMenuLateral;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Sistema Inteligente de Control de Cocina Casera (Escritorio)");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setLayout(new BorderLayout());

        // --- MENÚ LATERAL (Oculto inicialmente en el Login) ---
        panelMenuLateral = new JPanel();
        panelMenuLateral.setLayout(new BoxLayout(panelMenuLateral, BoxLayout.Y_AXIS));
        panelMenuLateral.setBackground(new Color(30, 41, 59));
        panelMenuLateral.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelMenuLateral.setPreferredSize(new Dimension(270, getHeight()));

        JLabel lblMenuTitulo = new JLabel("MÓDULOS DEL SISTEMA");
        lblMenuTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMenuTitulo.setForeground(new Color(148, 163, 184));
        lblMenuTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelMenuLateral.add(lblMenuTitulo);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 15)));

        // Creación de botones del menú lateral
        JButton btnDashboard = crearBotonMenu("Dashboard / Stock");
        JButton btnRecetasLista = crearBotonMenu("Ver Recetas");
        JButton btnIngrediente = crearBotonMenu("Registrar Ingrediente");
        JButton btnReceta = crearBotonMenu("Alta de Receta");
        JButton btnTransacciones = crearBotonMenu("Transacciones");
        JButton btnHistorial = crearBotonMenu("Historial Movimientos"); // NUEVO BOTÓN
        JButton btnPlanificador = crearBotonMenu("Planificador Compras");
        
        JButton btnLogout = crearBotonMenu("Cerrar Sesión");
        btnLogout.setForeground(new Color(252, 165, 165));

        // Añadir botones al menú lateral con espacios uniformes
        panelMenuLateral.add(btnDashboard);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnRecetasLista);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnIngrediente);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnReceta);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnTransacciones);
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnHistorial); // NUEVO ENLACE EN MENÚ
        panelMenuLateral.add(Box.createRigidArea(new Dimension(0, 6)));
        panelMenuLateral.add(btnPlanificador);
        panelMenuLateral.add(Box.createVerticalGlue());
        panelMenuLateral.add(btnLogout);

        add(panelMenuLateral, BorderLayout.WEST);

        // --- CONTENEDOR CENTRAL DINÁMICO (CardLayout) ---
        cardLayout = new CardLayout();
        panelContenidoCentral = new JPanel(cardLayout);

        // Pantalla de Login con evento para desplegar el menú al acceder
        PanelLogin panelLogin = new PanelLogin(e -> {
            panelMenuLateral.setVisible(true);
            cambiarVista("DASHBOARD");
        });

        // Registro de todos los paneles en el CardLayout
        panelContenidoCentral.add(panelLogin, "LOGIN");
        panelContenidoCentral.add(new PanelDashboard(), "DASHBOARD");
        panelContenidoCentral.add(new PanelListaRecetas(), "RECETAS_LISTA");
        panelContenidoCentral.add(new PanelIngrediente(), "INGREDIENTE");
        panelContenidoCentral.add(new PanelReceta(), "RECETA");
        panelContenidoCentral.add(new PanelTransacciones(), "TRANSACCIONES");
        panelContenidoCentral.add(new PanelHistorialTransaccion(), "HISTORIAL"); // NUEVO PANEL REGISTRADO
        panelContenidoCentral.add(new PanelPlanificador(), "PLANIFICADOR");

        add(panelContenidoCentral, BorderLayout.CENTER);

        // --- EVENTOS DE NAVEGACIÓN ---
        btnDashboard.addActionListener(e -> cambiarVista("DASHBOARD"));
        btnRecetasLista.addActionListener(e -> cambiarVista("RECETAS_LISTA"));
        btnIngrediente.addActionListener(e -> cambiarVista("INGREDIENTE"));
        btnReceta.addActionListener(e -> cambiarVista("RECETA"));
        btnTransacciones.addActionListener(e -> cambiarVista("TRANSACCIONES"));
        btnHistorial.addActionListener(e -> cambiarVista("HISTORIAL")); // NUEVO LISTENER ASIGNADO
        btnPlanificador.addActionListener(e -> cambiarVista("PLANIFICADOR"));

        btnLogout.addActionListener(e -> {
            panelMenuLateral.setVisible(false); // Ocultar menú al volver al login
            cambiarVista("LOGIN");
        });

        // Estado inicial de la aplicación
        panelMenuLateral.setVisible(false);
        cambiarVista("LOGIN");
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(240, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cambiarVista(String nombreVista) {
        cardLayout.show(panelContenidoCentral, nombreVista);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}