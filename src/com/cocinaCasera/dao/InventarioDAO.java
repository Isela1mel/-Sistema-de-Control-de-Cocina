package com.cocinaCasera.dao;

import com.cocinaCasera.config.ConexionBD;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/** Operaciones transaccionales del inventario. Las cantidades usan la unidad contable del ingrediente. */
public class InventarioDAO {
    public static class Ingrediente { public final int id; public final String nombre, unidad; public Ingrediente(int i,String n,String u){id=i;nombre=n;unidad=u;} public String toString(){return nombre+" ("+unidad+")";} }
    public static class Stock { public final int idIngrediente; public final String nombre, unidad, fecha, estado, notas; public final BigDecimal cantidad; public Stock(int id,String n,String u,BigDecimal c,String f,String e,String no){idIngrediente=id;nombre=n;unidad=u;cantidad=c;fecha=f;estado=e;notas=no;} }
    public static class Receta { public final int id, porciones; public final String nombre; public Receta(int i,String n,int p){id=i;nombre=n;porciones=p;} public String toString(){return nombre+" ("+porciones+" porciones base)";} }

    public List<Ingrediente> ingredientes() throws SQLException {
        List<Ingrediente> out=new ArrayList<>(); String q="SELECT i.id_ingrediente,i.nombre,u.abreviatura FROM ingrediente i JOIN unidad_medida u ON u.id_unidad=i.id_unidad WHERE i.activo=1 ORDER BY i.nombre";
        try(Connection c=ConexionBD.conectar();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())out.add(new Ingrediente(r.getInt(1),r.getString(2),r.getString(3)));} return out;
    }
    public List<Stock> stock() throws SQLException {
        List<Stock> out=new ArrayList<>();
        String q="SELECT i.id_ingrediente,i.nombre,u.abreviatura,COALESCE(SUM(CASE WHEN l.fecha_caducidad IS NULL OR l.fecha_caducidad>=CURRENT_DATE() THEN l.cantidad_actual ELSE 0 END),0) disponible, MIN(CASE WHEN l.cantidad_actual>0 AND (l.fecha_caducidad IS NULL OR l.fecha_caducidad>=CURRENT_DATE()) THEN l.fecha_caducidad END) proxima, MAX(l.notas) notas FROM ingrediente i JOIN unidad_medida u ON u.id_unidad=i.id_unidad LEFT JOIN inventario_lote l ON l.id_ingrediente=i.id_ingrediente WHERE i.activo=1 GROUP BY i.id_ingrediente,i.nombre,u.abreviatura ORDER BY i.nombre";
        try(Connection c=ConexionBD.conectar();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next()){java.sql.Date d=r.getDate("proxima"); BigDecimal n=r.getBigDecimal("disponible"); String caducidad=n.signum()<=0?"—":(d==null?"Sin fecha":d.toString()); out.add(new Stock(r.getInt(1),r.getString(2),r.getString(3),n,caducidad,n.signum()>0?"Disponible":"Sin stock",r.getString("notas")));}} return out;
    }
    public void altaIngrediente(String nombre,String unidad,Integer dias,String notas,BigDecimal inicial,LocalDate caducidad) throws SQLException {
        if(inicial.signum()<0)throw new IllegalArgumentException("La existencia inicial no puede ser negativa.");
        try(Connection c=ConexionBD.conectar()) { c.setAutoCommit(false); try {
            int id; String q="INSERT INTO ingrediente(nombre,id_unidad,dias_vida_util,origen,notas) SELECT ?,id_unidad,?,'PERSONALIZADO',? FROM unidad_medida WHERE abreviatura=?";
            try(PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){p.setString(1,nombre.trim());if(dias==null)p.setNull(2,Types.SMALLINT);else p.setInt(2,dias);p.setString(3,notas);p.setString(4,unidad);if(p.executeUpdate()!=1)throw new SQLException("Unidad de medida no encontrada: "+unidad);try(ResultSet k=p.getGeneratedKeys()){k.next();id=k.getInt(1);}}
            if(inicial.signum()>0) crearEntrada(c,id,inicial,caducidad,"Existencia inicial de ingrediente: "+nombre);
            c.commit();
        }catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);} }
    }
    public void compra(int id,BigDecimal cantidad,LocalDate cad,String descripcion) throws SQLException { if(cantidad.signum()<=0)throw new IllegalArgumentException("La cantidad debe ser mayor que cero."); try(Connection c=ConexionBD.conectar()){c.setAutoCommit(false);try{crearEntrada(c,id,cantidad,cad,descripcion==null?"Compra de ingrediente":descripcion);c.commit();}catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}} }
    private void crearEntrada(Connection c,int id,BigDecimal cantidad,LocalDate cad,String descripcion)throws SQLException{
        long tx;try(PreparedStatement p=c.prepareStatement("INSERT INTO transaccion(tipo,descripcion) VALUES('ENTRADA_COMPRA',?)",Statement.RETURN_GENERATED_KEYS)){p.setString(1,descripcion);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();tx=r.getLong(1);}}
        long lote;try(PreparedStatement p=c.prepareStatement("INSERT INTO inventario_lote(id_ingrediente,cantidad_inicial,cantidad_actual,fecha_caducidad,notas) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){p.setInt(1,id);p.setBigDecimal(2,cantidad);p.setBigDecimal(3,cantidad);if(cad==null)p.setNull(4,Types.DATE);else p.setDate(4,java.sql.Date.valueOf(cad));p.setString(5,descripcion);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();lote=r.getLong(1);}}
        try(PreparedStatement p=c.prepareStatement("INSERT INTO detalle_transaccion(id_transaccion,id_lote,cantidad) VALUES(?,?,?)")){p.setLong(1,tx);p.setLong(2,lote);p.setBigDecimal(3,cantidad);p.executeUpdate();}
    }
    public void merma(int id,BigDecimal cantidad,String descripcion) throws SQLException { moverStock(id,cantidad,"MERMA",descripcion); }
    private void moverStock(int id,BigDecimal cantidad,String tipo,String descripcion)throws SQLException{
        if(cantidad.signum()<=0)throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        try(Connection c=ConexionBD.conectar()){c.setAutoCommit(false);try{long tx;try(PreparedStatement p=c.prepareStatement("INSERT INTO transaccion(tipo,descripcion) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS)){p.setString(1,tipo);p.setString(2,descripcion);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();tx=r.getLong(1);}}
            descontar(c,tx,id,cantidad);c.commit();}catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}}
    }
    public void consumirReceta(int idReceta,int porciones) throws SQLException {
        if(porciones<=0)throw new IllegalArgumentException("Las porciones deben ser mayores que cero.");
        try(Connection c=ConexionBD.conectar()){c.setAutoCommit(false);try{
            int base;try(PreparedStatement p=c.prepareStatement("SELECT porciones_base FROM receta WHERE id_receta=? AND activa=1 FOR UPDATE")){p.setInt(1,idReceta);try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("La receta no existe o está inactiva.");base=r.getInt(1);}}
            Map<Integer,BigDecimal> req=new LinkedHashMap<>();try(PreparedStatement p=c.prepareStatement("SELECT id_ingrediente,cantidad_requerida FROM receta_ingrediente WHERE id_receta=?")){p.setInt(1,idReceta);try(ResultSet r=p.executeQuery()){while(r.next())req.put(r.getInt(1),r.getBigDecimal(2).multiply(BigDecimal.valueOf(porciones)).divide(BigDecimal.valueOf(base),3,java.math.RoundingMode.HALF_UP));}}
            if(req.isEmpty())throw new SQLException("La receta no tiene ingredientes asociados.");
            // Prevalidación completa antes de descontar: operación atómica.
            for(Map.Entry<Integer,BigDecimal> e:req.entrySet()) if(stockDisponible(c,e.getKey()).compareTo(e.getValue())<0)throw new SQLException("Stock insuficiente para ingrediente ID "+e.getKey()+". Se canceló todo el consumo.");
            long tx;try(PreparedStatement p=c.prepareStatement("INSERT INTO transaccion(tipo,id_receta,porciones_preparadas,descripcion) VALUES('CONSUMO_RECETA',?,?,?)",Statement.RETURN_GENERATED_KEYS)){p.setInt(1,idReceta);p.setInt(2,porciones);p.setString(3,"Preparación de receta ID "+idReceta+" ("+porciones+" porciones)");p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();tx=r.getLong(1);}}
            for(Map.Entry<Integer,BigDecimal> e:req.entrySet())descontar(c,tx,e.getKey(),e.getValue()); c.commit();
        }catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}}
    }
    private BigDecimal stockDisponible(Connection c,int id)throws SQLException{try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(cantidad_actual),0) FROM inventario_lote WHERE id_ingrediente=? AND cantidad_actual>0 AND (fecha_caducidad IS NULL OR fecha_caducidad>=CURRENT_DATE()) FOR UPDATE")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){r.next();return r.getBigDecimal(1);}}}
    private void descontar(Connection c,long tx,int id,BigDecimal cantidad)throws SQLException{
        BigDecimal restante=cantidad;String q="SELECT id_lote,cantidad_actual FROM inventario_lote WHERE id_ingrediente=? AND cantidad_actual>0 AND (fecha_caducidad IS NULL OR fecha_caducidad>=CURRENT_DATE()) ORDER BY fecha_caducidad IS NULL,fecha_caducidad,id_lote FOR UPDATE";
        try(PreparedStatement p=c.prepareStatement(q)){p.setInt(1,id);try(ResultSet r=p.executeQuery()){while(r.next()&&restante.signum()>0){long lote=r.getLong(1);BigDecimal disponible=r.getBigDecimal(2);BigDecimal usar=disponible.min(restante);try(PreparedStatement u=c.prepareStatement("UPDATE inventario_lote SET cantidad_actual=cantidad_actual-? WHERE id_lote=? AND cantidad_actual>=?")){u.setBigDecimal(1,usar);u.setLong(2,lote);u.setBigDecimal(3,usar);if(u.executeUpdate()!=1)throw new SQLException("No se pudo actualizar el lote "+lote);}
            try(PreparedStatement d=c.prepareStatement("INSERT INTO detalle_transaccion(id_transaccion,id_lote,cantidad) VALUES(?,?,?)")){d.setLong(1,tx);d.setLong(2,lote);d.setBigDecimal(3,usar);d.executeUpdate();} restante=restante.subtract(usar);}}}
        if(restante.signum()>0)throw new SQLException("Stock insuficiente. Operación cancelada.");
    }
    public List<Receta> recetas() throws SQLException {List<Receta> out=new ArrayList<>();try(Connection c=ConexionBD.conectar();PreparedStatement p=c.prepareStatement("SELECT id_receta,nombre,porciones_base FROM receta WHERE activa=1 ORDER BY nombre");ResultSet r=p.executeQuery()){while(r.next())out.add(new Receta(r.getInt(1),r.getString(2),r.getInt(3)));}return out;}
    public void guardarReceta(String nombre,int porciones,String instrucciones,Map<Integer,BigDecimal> ingredientes)throws SQLException{
        if(nombre.trim().isEmpty()||porciones<1||ingredientes.isEmpty())throw new IllegalArgumentException("Indica nombre, porciones e ingredientes.");
        try(Connection c=ConexionBD.conectar()){c.setAutoCommit(false);try{int id;try(PreparedStatement p=c.prepareStatement("INSERT INTO receta(nombre,porciones_base,instrucciones,activa) VALUES(?,?,?,1)",Statement.RETURN_GENERATED_KEYS)){p.setString(1,nombre.trim());p.setInt(2,porciones);p.setString(3,instrucciones);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();id=r.getInt(1);}}
            try(PreparedStatement p=c.prepareStatement("INSERT INTO receta_ingrediente(id_receta,id_ingrediente,cantidad_requerida) VALUES(?,?,?)")){for(Map.Entry<Integer,BigDecimal> e:ingredientes.entrySet()){p.setInt(1,id);p.setInt(2,e.getKey());p.setBigDecimal(3,e.getValue());p.addBatch();}p.executeBatch();}c.commit();}catch(Exception e){c.rollback();if(e instanceof SQLException)throw (SQLException)e;throw new SQLException(e);}}
    }
    
    public List<Object[]> listaRecetas() throws SQLException {

        List<Object[]> out = new ArrayList<>();

        String q =
            "SELECT r.id_receta, " +
            "r.nombre, " +
            "r.porciones_base, " +
            "r.imagen_url, " +
            "COALESCE(GROUP_CONCAT(" +
            "CONCAT(i.nombre,' (',ri.cantidad_requerida,' ',u.abreviatura,')') " +
            "ORDER BY i.nombre SEPARATOR '\n'), " +
            "'Sin ingredientes asociados'), " +
            "COALESCE(r.instrucciones,'') " +
            "FROM receta r " +
            "LEFT JOIN receta_ingrediente ri " +
            "ON ri.id_receta = r.id_receta " +
            "LEFT JOIN ingrediente i " +
            "ON i.id_ingrediente = ri.id_ingrediente " +
            "LEFT JOIN unidad_medida u " +
            "ON u.id_unidad = i.id_unidad " +
            "WHERE r.activa = 1 " +
            "GROUP BY r.id_receta, r.nombre, " +
            "r.porciones_base, r.imagen_url, r.instrucciones " +
            "ORDER BY r.nombre";

        try (Connection c = ConexionBD.conectar();
            PreparedStatement p = c.prepareStatement(q);
            ResultSet r = p.executeQuery()) {

            while (r.next()) {

                int id = r.getInt("id_receta");

                // Ruta registrada en MySQL, si existe.
                String imagen = r.getString("imagen_url");

                // RUTA TEMPORAL:
                // Si imagen_url es NULL o está vacía,
                // se utiliza una imagen por defecto.
                if (imagen == null || imagen.isBlank()) {
                    imagen = "/com/cocinaCasera/resources/images/default.png";
                }

                out.add(new Object[]{
                    id,
                    imagen,
                    r.getString("nombre"),
                    r.getInt("porciones_base"),
                    r.getString(5),
                    r.getString(6)
                });
            }
        }

        return out;
    }

    public List<Object[]> capacidades() throws SQLException { List<Object[]> out=new ArrayList<>(); String q="SELECT r.id_receta,r.nombre,r.porciones_base,COALESCE(FLOOR(MIN(COALESCE(s.disponible,0)/ri.cantidad_requerida))*r.porciones_base,0) porciones_posibles FROM receta r JOIN receta_ingrediente ri ON ri.id_receta=r.id_receta LEFT JOIN (SELECT id_ingrediente,SUM(cantidad_actual) disponible FROM inventario_lote WHERE cantidad_actual>0 AND (fecha_caducidad IS NULL OR fecha_caducidad>=CURRENT_DATE()) GROUP BY id_ingrediente) s ON s.id_ingrediente=ri.id_ingrediente WHERE r.activa=1 GROUP BY r.id_receta,r.nombre,r.porciones_base ORDER BY r.nombre";try(Connection c=ConexionBD.conectar();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())out.add(new Object[]{r.getInt(1),r.getString(2),r.getInt(3),r.getLong(4)});}return out; }
    public List<Object[]> historial() throws SQLException {
        List<Object[]> out = new ArrayList<>();
        String q = "SELECT t.id_transaccion, t.tipo, COALESCE(r.nombre,t.descripcion) concepto, " +
                   "d.cantidad, i.nombre ingrediente, u.abreviatura unidad, t.fecha_hora " +
                   "FROM transaccion t " +
                   "LEFT JOIN receta r ON r.id_receta=t.id_receta " +
                   "LEFT JOIN detalle_transaccion d ON d.id_transaccion=t.id_transaccion " +
                   "LEFT JOIN inventario_lote l ON l.id_lote=d.id_lote " +
                   "LEFT JOIN ingrediente i ON i.id_ingrediente=l.id_ingrediente " +
                   "LEFT JOIN unidad_medida u ON u.id_unidad=i.id_unidad " +
                   "ORDER BY t.fecha_hora DESC, t.id_transaccion DESC, i.nombre";
        try (Connection c = ConexionBD.conectar();
             PreparedStatement p = c.prepareStatement(q);
             ResultSet r = p.executeQuery()) {
            long currentId = -1;
            String currentType = null, currentConcept = null;
            Timestamp currentDate = null;
            List<String> quantityDetails = new ArrayList<>();
            BigDecimal singleQuantity = BigDecimal.ZERO;
            String singleUnit = "";
            int detailCount = 0;
            while (r.next()) {
                long id = r.getLong(1);
                if (currentId != -1 && id != currentId) {
                    out.add(new Object[]{currentId, currentType, currentConcept,
                            formatHistoryQuantity(currentType, quantityDetails, singleQuantity, singleUnit, detailCount), currentDate});
                    quantityDetails = new ArrayList<>();
                    singleQuantity = BigDecimal.ZERO;
                    singleUnit = "";
                    detailCount = 0;
                }
                if (id != currentId) {
                    currentId = id;
                    currentType = r.getString(2);
                    currentConcept = r.getString(3);
                    currentDate = r.getTimestamp(7);
                }
                BigDecimal amount = r.getBigDecimal(4);
                String ingredient = r.getString(5);
                String unit = r.getString(6);
                if (amount != null && ingredient != null) {
                    String amountText = amount.stripTrailingZeros().toPlainString();
                    quantityDetails.add(ingredient + ": " + amountText + (unit == null || unit.isBlank() ? "" : " " + unit));
                    singleQuantity = singleQuantity.add(amount);
                    singleUnit = unit == null ? "" : unit;
                    detailCount++;
                }
            }
            if (currentId != -1) {
                out.add(new Object[]{currentId, currentType, currentConcept,
                        formatHistoryQuantity(currentType, quantityDetails, singleQuantity, singleUnit, detailCount), currentDate});
            }
        }
        return out;
    }

    private String formatHistoryQuantity(String type, List<String> details, BigDecimal total, String unit, int count) {
        if (count == 0) return "—";
        // En consumo de receta no se suman ingredientes ni unidades diferentes.
        if ("CONSUMO_RECETA".equals(type) || count > 1) return String.join("; ", details);
        return total.stripTrailingZeros().toPlainString() + (unit == null || unit.isBlank() ? "" : " " + unit);
    }
}
