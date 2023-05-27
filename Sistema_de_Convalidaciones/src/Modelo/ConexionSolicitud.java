package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionSolicitud {

    private Connection conn;

    public ConexionSolicitud() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "Solicitudes" existe y crearla si no
            if (!tablaExiste("Solicitudes")) {
                crearTablaSolicitudes();
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

    private void crearTablaSolicitudes() throws SQLException {
        if (tablaExiste("SOLICITUDES") == false) {
            String createQuery = "CREATE TABLE Solicitudes ("
                    + "tecnologicoProcedencia VARCHAR(50) NOT NULL,"
                    + "noControl VARCHAR(10) NOT NULL,"
                    + "semestre INT NOT NULL,"
                    + "carreraActual VARCHAR(50) NOT NULL,"
                    + "carreraSolicitada VARCHAR(50) NOT NULL,"
                    + "claveCarrera VARCHAR(10) NOT NULL,"
                    + "celular VARCHAR(15) NOT NULL,"
                    + "email VARCHAR(50) NOT NULL"
                    + ")";

            try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al crear la tabla Solicitudes");
            }
        }

    }

    public void insertarSolicitud(String tecnologicoProcedencia, String noControl, int semestre,
            String carreraActual, String carreraSolicitada, String claveCarrera,
            String celular, String email) {
        // Verificar si el número de control ya hizo una solicitud
        if (solicitudExistente(noControl)) {
            JOptionPane.showMessageDialog(null, "Este usuario ya ha realizado una solicitud.");
            return;
        }

        String insertQuery = "INSERT INTO Solicitudes (tecnologicoProcedencia, noControl, semestre, "
                + "carreraActual, carreraSolicitada, claveCarrera, celular, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.setString(1, tecnologicoProcedencia);
            statement.setString(2, noControl);
            statement.setInt(3, semestre);
            statement.setString(4, carreraActual);
            statement.setString(5, carreraSolicitada);
            statement.setString(6, claveCarrera);
            statement.setString(7, celular);
            statement.setString(8, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar la solicitud");
        }
    }

    public boolean solicitudExistente(String noControl) {
        String selectQuery = "SELECT * FROM Solicitudes WHERE noControl = ?";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, noControl);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void mostrarSolicitudes() {
        String selectQuery = "SELECT * FROM Solicitudes";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla Solicitudes:");
            while (resultSet.next()) {
                String tecnologicoProcedencia = resultSet.getString("tecnologicoProcedencia");
                String noControl = resultSet.getString("noControl");
                int semestre = resultSet.getInt("semestre");
                String carreraActual = resultSet.getString("carreraActual");
                String carreraSolicitada = resultSet.getString("carreraSolicitada");
                String claveCarrera = resultSet.getString("claveCarrera");
                String celular = resultSet.getString("celular");
                String email = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla Solicitudes");
        }
    }

    public void eliminarSolicitudes() {
        String deleteQuery = "DELETE FROM Solicitudes";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar las solicitudes");
        }
    }

    public String obtenerNumerosControl() {
        String selectQuery = "SELECT noControl FROM Solicitudes";
        StringBuilder numerosControl = new StringBuilder();

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String noControl = resultSet.getString("noControl");
                numerosControl.append(noControl).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los números de control de la tabla Solicitudes");
        }

        return numerosControl.toString();
    }

    public String obtenerTecnologicoProcedencia() {
        String selectQuery = "SELECT tecnologicoProcedencia FROM Solicitudes";
        StringBuilder tecnologicos = new StringBuilder();

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String tecnologico = resultSet.getString("tecnologicoProcedencia");
                tecnologicos.append(tecnologico).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los tecnológicos de procedencia de la tabla Solicitudes");
        }

        return tecnologicos.toString();
    }

}
