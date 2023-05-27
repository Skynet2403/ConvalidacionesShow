package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionCalificaciones {
    private Connection conn;

    public ConexionCalificaciones() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "CALIFICACIONES" existe y crearla si no
            if (!tablaExiste("CALIFICACIONES")) {
                crearTablaCalificaciones();
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

    public void insertarCalificacion(String materia, String calificacion) {
        String insertQuery = "INSERT INTO CALIFICACIONES (materia, calificacion) VALUES (?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.setString(1, materia);
            statement.setString(2, calificacion);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar los datos en la tabla CALIFICACIONES");
        }
    }

    public void mostrarCalificaciones() {
        String selectQuery = "SELECT * FROM CALIFICACIONES";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla CALIFICACIONES:");
            while (resultSet.next()) {
                String materia = resultSet.getString("materia");
                String calificacion = resultSet.getString("calificacion");
                System.out.println(materia + " - " + calificacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla CALIFICACIONES");
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

    private void crearTablaCalificaciones() throws SQLException {
        String createQuery = "CREATE TABLE CALIFICACIONES (materia VARCHAR(100), calificacion VARCHAR(10))";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla CALIFICACIONES");
        }
    }
    
    public void eliminarTodasCalificaciones() {
    String deleteQuery = "DELETE FROM CALIFICACIONES";

    try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
        } else {
            System.out.println("No se encontraron calificaciones para eliminar.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error al eliminar las calificaciones");
    }
}
    
    public String[] obtenerCalificaciones() {
    String[] calificaciones = null;

    try {
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("SELECT calificacion FROM CALIFICACIONES");

        // Obtener la cantidad de filas en el resultado
        rs.last();
        int rowCount = rs.getRow();

        // Crear un array con la cantidad de filas obtenidas
        calificaciones = new String[rowCount];

        // Volver al inicio del resultado
        rs.beforeFirst();

        // Llenar el array con los valores obtenidos
        int i = 0;
        while (rs.next()) {
            String calificacion = rs.getString("calificacion");
            calificaciones[i] = calificacion;
            i++;
        }

        rs.close();
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return calificaciones;
}

}
