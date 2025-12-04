package com.example.proyectofinalicc211.data;

import com.example.proyectofinalicc211.model.Parada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParadaDAO {

    public List<Parada> obtenerTodas() {
        List<Parada> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, x, y FROM paradas";

        try (Connection conn = ConeccionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                Parada p = new Parada(id, nombre);
                p.setPosicionX(x);
                p.setPosicionY(y);
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardar(Parada parada) {
        String sql = "INSERT INTO paradas (id, nombre, x, y) VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET nombre = ?, x = ?, y = ?";

        try (Connection conn = ConeccionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, parada.getId());
            pstmt.setString(2, parada.getNombre());
            pstmt.setDouble(3, parada.getPosicionX());
            pstmt.setDouble(4, parada.getPosicionY());
            pstmt.setString(5, parada.getNombre());
            pstmt.setDouble(6, parada.getPosicionX());
            pstmt.setDouble(7, parada.getPosicionY());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(UUID id) {
        String sql = "DELETE FROM paradas WHERE id = ?";
        try (Connection conn = ConeccionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}