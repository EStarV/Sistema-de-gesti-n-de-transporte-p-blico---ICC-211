package com.example.proyectofinalicc211.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConeccionBD {
    private static final String URL = "jdbc:postgresql://db.ferabijlyjoqrdkhkljq.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "SUMA-System1900";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}