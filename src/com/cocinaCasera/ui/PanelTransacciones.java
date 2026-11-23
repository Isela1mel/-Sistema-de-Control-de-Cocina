package com.cocinaCasera.ui;

import com.cocinaCasera.dao.InventarioDAO;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Compras, mermas y preparación de recetas con persistencia y transacciones SQL. */
public class PanelTransacciones extends JPanel {
    private final InventarioDAO dao=new InventarioDAO();
    private final JComboBox<String> tipo=new JComboBox<>(new String[]{"Entrada por compra","Merma / desperdicio","Consumo por receta"});
    private final JComboBox<InventarioDAO.Ingrediente> ingrediente=new JComboBox<>();
    private final JComboBox<InventarioDAO.Receta> receta=new JComboBox<>();
    private final JTextField cantidad=new JTextField("100",16), fecha=new JTextField(LocalDate.now().plusDays(30).toString(),16), descripcion=new JTextField("",16);
    private final JLabel lblCantidad=new JLabel("Cantidad:"),lblFecha=new JLabel("Caducidad (AAAA-MM-DD):");
    private final CardLayout selectorLayout=new CardLayout(); private final JPanel selector=new JPanel(selectorLayout);
    public PanelTransacciones(){setLayout(new BorderLayout(18,18));setBackground(new Color(241,245,249));setBorder(BorderFactory.createEmptyBorder(25,30,25,30));JLabel t=new JLabel("Módulo Transaccional de Inventario");t.setFont(new Font("Segoe UI",Font.BOLD,26));add(t,BorderLayout.NORTH);
        JPanel card=new JPanel(new GridBagLayout());card.setBackground(Color.WHITE);card.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(9,9,9,9);g.fill=GridBagConstraints.HORIZONTAL;g.gridx=0;g.gridy=0;card.add(new JLabel("Tipo de movimiento:"),g);g.gridx=1;card.add(tipo,g);
        selector.add(ingrediente,"ING");selector.add(receta,"REC");g.gridx=0;g.gridy=1;card.add(new JLabel("Ingrediente / receta:"),g);g.gridx=1;card.add(selector,g);
        g.gridx=0;g.gridy=2;card.add(lblCantidad,g);g.gridx=1;card.add(cantidad,g);
        g.gridx=0;g.gridy=3;card.add(lblFecha,g);g.gridx=1;card.add(fecha,g);
        g.gridx=0;g.gridy=4;card.add(new JLabel("Descripción:"),g);g.gridx=1;card.add(descripcion,g);
        JButton limpiar=new JButton("Limpiar"),procesar=new JButton("Procesar movimiento");JPanel botones=new JPanel(new FlowLayout(FlowLayout.RIGHT));botones.setOpaque(false);botones.add(limpiar);botones.add(procesar);g.gridx=1;g.gridy=5;card.add(botones,g);add(card,BorderLayout.CENTER);
        tipo.addActionListener(e->actualizarTipo());limpiar.addActionListener(e->{cantidad.setText("100");descripcion.setText("");});procesar.addActionListener(e->procesar());cargarCombos();actualizarTipo();
    }
    private void cargarCombos(){try{ingrediente.removeAllItems();for(var x:dao.ingredientes())ingrediente.addItem(x);receta.removeAllItems();for(var x:dao.recetas())receta.addItem(x);}catch(SQLException e){error(e);}}
    private void actualizarTipo(){boolean consumo=tipo.getSelectedIndex()==2;selectorLayout.show(selector,consumo?"REC":"ING");lblCantidad.setVisible(!consumo);cantidad.setVisible(!consumo);lblFecha.setVisible(tipo.getSelectedIndex()==0);fecha.setVisible(tipo.getSelectedIndex()==0);}
    private void procesar(){try{int t=tipo.getSelectedIndex();if(t==2){InventarioDAO.Receta r=(InventarioDAO.Receta)receta.getSelectedItem();if(r==null)throw new IllegalArgumentException("No hay recetas registradas.");int p=Integer.parseInt(JOptionPane.showInputDialog(this,"¿Cuántas porciones deseas preparar?",r.porciones));dao.consumirReceta(r.id,p);JOptionPane.showMessageDialog(this,"Receta preparada. El inventario se descontó correctamente.");}
        else{InventarioDAO.Ingrediente i=(InventarioDAO.Ingrediente)ingrediente.getSelectedItem();if(i==null)throw new IllegalArgumentException("No hay ingredientes registrados.");BigDecimal q=new BigDecimal(cantidad.getText().trim().replace(",","."));String desc=descripcion.getText().trim();if(t==0){LocalDate d=fecha.getText().trim().isEmpty()?null:LocalDate.parse(fecha.getText().trim());dao.compra(i.id,q,d,desc.isEmpty()?"Compra de "+i.nombre:desc);JOptionPane.showMessageDialog(this,"Compra registrada y stock actualizado.");}else{dao.merma(i.id,q,desc.isEmpty()?"Merma de "+i.nombre:desc);JOptionPane.showMessageDialog(this,"Merma registrada y stock actualizado.");}}
        cantidad.setText("100");descripcion.setText("");
    }catch(NumberFormatException|DateTimeParseException e){JOptionPane.showMessageDialog(this,"Revisa la cantidad o la fecha. Usa formato AAAA-MM-DD.","Datos inválidos",JOptionPane.WARNING_MESSAGE);}catch(Exception e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,"No se pudo procesar el movimiento:\n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
}
