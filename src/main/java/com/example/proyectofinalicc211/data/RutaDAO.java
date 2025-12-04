package com.example.proyectofinalicc211.data;

import com.example.proyectofinalicc211.model.Ruta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RutaDAO {

    public List<Ruta> obtenerTodas() {
        List<Ruta> lista = new ArrayList<>();
        String sql = "SELECT id, id_origen, id_destino, tiempo, costo, distancia, transbordos, medio FROM rutas";

        try (Connection conn = ConeccionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ruta r = new Ruta(
                        UUID.fromString(rs.getString("id_origen")),
                        UUID.fromString(rs.getString("id_destino")),
                        rs.getInt("tiempo"),
                        rs.getInt("costo"),
                        rs.getDouble("distancia"),
                        rs.getInt("transbordos"),
                        rs.getString("medio")
                );
                r.setId(UUID.fromString(rs.getString("id")));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardar(Ruta ruta) {
        String sql = "INSERT INTO rutas (id, id_origen, id_destino, tiempo, costo, distancia, transbordos, medio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET " +
                "id_origen = ?, id_destino = ?, tiempo = ?, costo = ?, distancia = ?, transbordos = ?, medio = ?";

        try (Connection conn = ConeccionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, ruta.getId());
            pstmt.setObject(2, ruta.getId_origen());
            pstmt.setObject(3, ruta.getId_destino());
            pstmt.setInt(4, ruta.getTiempo());
            pstmt.setInt(5, ruta.getCosto());
            pstmt.setDouble(6, ruta.getDistancia());
            pstmt.setInt(7, ruta.getTransbordos());
            pstmt.setString(8, ruta.getMedio());

            pstmt.setObject(9, ruta.getId_origen());
            pstmt.setObject(10, ruta.getId_destino());
            pstmt.setInt(11, ruta.getTiempo());
            pstmt.setInt(12, ruta.getCosto());
            pstmt.setDouble(13, ruta.getDistancia());
            pstmt.setInt(14, ruta.getTransbordos());
            pstmt.setString(15, ruta.getMedio());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(UUID id) {
        String sql = "DELETE FROM rutas WHERE id = ?";
        try (Connection conn = ConeccionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}