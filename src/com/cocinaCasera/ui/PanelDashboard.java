package com.cocinaCasera.ui;

import com.cocinaCasera.dao.InventarioDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/** Dashboard alimentado directamente por los lotes vigentes de MySQL. */
public class PanelDashboard extends JPanel {
    private final InventarioDAO dao = new InventarioDAO();
    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Ingrediente", "Existencia disponible", "Próxima caducidad", "Estado", "Notas"},0){public boolean isCellEditable(int r,int c){return false;}};
    public PanelDashboard(){setLayout(new BorderLayout(18,18));setBackground(new Color(241,245,249));setBorder(BorderFactory.createEmptyBorder(25,30,25,30));JLabel t=new JLabel("Dashboard e Inventario de Stock");t.setFont(new Font("Segoe UI",Font.BOLD,26));add(t,BorderLayout.NORTH);
        JTable tabla=new JTable(modelo);tabla.setRowHeight(29);tabla.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));JPanel card=new JPanel(new BorderLayout(10,10));card.setBackground(Color.WHITE);card.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));card.add(new JLabel("Existencias calculadas desde inventario_lote. Los lotes vencidos se excluyen."),BorderLayout.NORTH);card.add(new JScrollPane(tabla),BorderLayout.CENTER);JButton actualizar=new JButton("Actualizar stock");actualizar.addActionListener(e->actualizar());JPanel sur=new JPanel(new FlowLayout(FlowLayout.RIGHT));sur.add(actualizar);card.add(sur,BorderLayout.SOUTH);add(card,BorderLayout.CENTER);actualizar();}
    public final void actualizar(){try{modelo.setRowCount(0);for(InventarioDAO.Stock s:dao.stock())modelo.addRow(new Object[]{s.idIngrediente,s.nombre,s.cantidad.stripTrailingZeros().toPlainString()+" "+s.unidad,s.fecha,s.estado,s.notas==null?"":s.notas});}catch(SQLException e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,"No se pudo consultar el inventario:\n"+e.getMessage(),"MySQL",JOptionPane.ERROR_MESSAGE);}
}
