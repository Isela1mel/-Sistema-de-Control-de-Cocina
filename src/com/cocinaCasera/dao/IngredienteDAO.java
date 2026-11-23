package com.cocinaCasera.dao;

import com.cocinaCasera.config.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a datos de la tabla ingrediente. */
public class IngredienteDAO {
    public static class IngredienteFila {
        public final int id;
        public final String nombre;
        public final String unidad;
        public final Object diasVida;
        public final String origen;
        public final String notas;

        public IngredienteFila(int id, String nombre, String unidad, Object diasVida, String origen, String notas) {
            this.id = id; this.nombre = nombre; this.unidad = unidad;
            this.diasVida = diasVida; this.origen = origen; this.notas = notas;
        }
    }

    public List<IngredienteFila> listarActivos() throws SQLException {
        String sql = "SELECT i.id_ingrediente, i.nombre, u.abreviatura, i.dias_vida_util, i.origen, i.notas "
                   + "FROM ingrediente i JOIN unidad_medida u ON u.id_unidad=i.id_unidad "
                   + "WHERE i.activo=TRUE ORDER BY i.nombre";
        List<IngredienteFila> lista = new ArrayList<>();
        try (Connection cn = ConexionBD.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new IngredienteFila(rs.getInt("id_ingrediente"), rs.getString("nombre"),
                    rs.getString("abreviatura"), rs.getObject("dias_vida_util"),
                    rs.getString("origen"), rs.getString("notas")));
            }
        }
        return lista;
    }

    public void insertar(String nombre, String unidad, Integer diasVida, String notas) throws SQLException {
        String sql = "INSERT INTO ingrediente (nombre, id_unidad, dias_vida_util, origen, notas) "
                   + "SELECT ?, id_unidad, ?, 'PERSONALIZADO', ? FROM unidad_medida WHERE abreviatura=?";
        try (Connection cn = ConexionBD.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            if (diasVida == null) ps.setNull(2, Types.SMALLINT); else ps.setInt(2, diasVida);
            if (notas == null || notas.trim().isEmpty()) ps.setNull(3, Types.VARCHAR); else ps.setString(3, notas.trim());
            ps.setString(4, unidad);
            if (ps.executeUpdate() != 1) throw new SQLException("No se encontró la unidad de medida: " + unidad);
        }
    }
}
