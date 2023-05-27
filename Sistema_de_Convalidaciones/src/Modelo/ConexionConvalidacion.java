package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionConvalidacion {

    private Connection conn;

    public ConexionConvalidacion() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "COMPARACION_CARRERAS" existe y crearla si no
            if (!tablaExiste("COMPARACION_CARRERAS")) {
                crearTablaComparacionCarreras();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Revisa el driver de la base de datos");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void cerrarConexion() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tablaExiste(String nombreTabla) throws SQLException {
        boolean existe = false;
        String query = "SELECT 1 FROM SYS.SYSTABLES WHERE TABLENAME = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, nombreTabla);
            existe = statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    private void crearTablaComparacionCarreras() throws SQLException {
        String createQuery = "CREATE TABLE COMPARACION_CARRERAS ("
                + "carrera1 VARCHAR(100),"
                + "carrera2 VARCHAR(100)"
                + ")";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla COMPARACION_CARRERAS");
        }
    }

    public void insertarDatosEnTablaComparacion(String carrera1, String carrera2) {
        try {
            // Eliminar los datos existentes en la tabla COMPARACION_CARRERAS
            eliminarComparacionCarreras();

            // Insertar los datos en la tabla COMPARACION_CARRERAS
            String insertQuery = "INSERT INTO COMPARACION_CARRERAS (carrera1, carrera2) VALUES (?, ?)";

            try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                insertStatement.setString(1, carrera1);
                insertStatement.setString(2, carrera2);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar los datos en la tabla COMPARACION_CARRERAS");
        }
    }

    public void mostrarComparacionCarreras() {
        String selectQuery = "SELECT * FROM COMPARACION_CARRERAS";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla COMPARACION_CARRERAS:");
            while (resultSet.next()) {
                String carrera1 = resultSet.getString("carrera1");
                String carrera2 = resultSet.getString("carrera2");
                String materiasComunes = resultSet.getString("materias_comunes");
                System.out.println("Carrera 1: " + carrera1 + ", Carrera 2: " + carrera2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla COMPARACION_CARRERAS");
        }
    }

    public void eliminarComparacionCarreras() {
        String deleteQuery = "DELETE FROM COMPARACION_CARRERAS";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar las comparaciones de carreras");
        }
    }

    public String obtenerAsignaturaConvalidada() {
        String asignaturaConvalidada = "";

        try {
            // Preparar la consulta SQL para obtener la asignatura convalidada
            String query = "SELECT carrera2 FROM COMPARACION_CARRERAS";
            PreparedStatement statement = conn.prepareStatement(query);

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            // Verificar si se obtuvo un resultado
            if (resultSet.next()) {
                // Obtener el valor de la columna "carrera2"
                asignaturaConvalidada = resultSet.getString("carrera2");
            }

            // Cerrar el ResultSet y el Statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return asignaturaConvalidada;
    }
}
