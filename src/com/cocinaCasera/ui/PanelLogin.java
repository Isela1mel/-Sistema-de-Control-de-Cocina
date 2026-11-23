package com.cocinaCasera.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Pantalla de inicio de sesión con botón de alto contraste y diseño centrado.
 */
public class PanelLogin extends JPanel {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public PanelLogin(ActionListener onLoginSuccess) {
        setLayout(new GridBagLayout());
        setBackground(new Color(15, 23, 42)); // Fondo oscuro (#0f172a)

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        card.setPreferredSize(new Dimension(380, 330));

        JLabel lblTitulo = new JLabel("Acceso al Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Usuario (Admin Hogar):");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(71, 85, 105));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField("ADMIN_admin");
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setMaximumSize(new Dimension(320, 35));

        JLabel lblPass = new JLabel("Contraseña local:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(71, 85, 105));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField("12345");
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setMaximumSize(new Dimension(320, 35));

        // Botón con color fuerte y alto contraste para que se vea claramente
        JButton btnAcceder = new JButton("Acceder al Sistema");
        btnAcceder.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAcceder.setBackground(new Color(15, 23, 42)); // Oscuro (#0f172a)
        btnAcceder.setFocusPainted(false);
        btnAcceder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAcceder.setMaximumSize(new Dimension(320, 45));
        btnAcceder.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAcceder.addActionListener(e -> {
            if (!txtUsuario.getText().trim().isEmpty()) {
                onLoginSuccess.actionPerformed(e);
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese un usuario válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(lblUser);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtUsuario);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblPass);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(btnAcceder);

        add(card);
    }
}