package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConexionSeguimiento {
    private static final String URL = "jdbc:mysql://localhost:3306/nombre_basedatos";
    private static final String USERNAME = "usuario";
    private static final String PASSWORD = "contraseña";
    private Connection connection;

    public ConexionSeguimiento() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al conectar a la base de datos de seguimiento.");
        }
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la conexión.");
        }
    }

    public void insertarSeguimiento(String dato1, String dato2) {
        String insertQuery = "INSERT INTO tabla_seguimiento (columna1, columna2) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, dato1);
            statement.setString(2, dato2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el seguimiento.");
        }
    }

    public List<String> obtenerSeguimientos() {
        List<String> seguimientos = new ArrayList<>();
        String selectQuery = "SELECT columna1, columna2 FROM tabla_seguimiento";

        try (PreparedStatement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String dato1 = resultSet.getString("columna1");
                String dato2 = resultSet.getString("columna2");
                String seguimiento = "Dato1: " + dato1 + ", Dato2: " + dato2;
                seguimientos.add(seguimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los seguimientos.");
        }

        return seguimientos;
    }

    public void eliminarSeguimiento(String dato1) {
        String deleteQuery = "DELETE FROM tabla_seguimiento WHERE columna1 = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, dato1);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Seguimientos eliminados: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar los seguimientos.");
        }
    }
}
