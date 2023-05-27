package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionCarreras {

    private Connection conn;

    public ConexionCarreras() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "CARRERAS" existe y crearla si no
            if (!tablaExiste("CARRERAS")) {
                crearTablaCarreras();
            }
            
            insertarCarrera("Ingeniería de Sistemas Computacionales");
            insertarCarrera("Ingeniería Electrónica");
            insertarCarrera("Ingeniería Industrial");
            insertarCarrera("Ingeniería en Mecatrónica");
            insertarCarrera("Ingeniería en Informática");

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

    private void crearTablaCarreras() throws SQLException {
        String createQuery = "CREATE TABLE CARRERAS ("
                + "nombre VARCHAR(100),"
                + ")";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla CARRERAS");
        }
    }

    public void insertarCarrera(String nombre) {
        if (!carreraExiste(nombre)) {
            String insertQuery = "INSERT INTO CARRERAS (nombre) VALUES (?)";

            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setString(1, nombre);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar la carrera: " + nombre);
            }
        } else {
            
        }
    }

    private boolean carreraExiste(String nombre) {
        String selectQuery = "SELECT * FROM CARRERAS WHERE nombre = ?";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void mostrarCarreras() {
        String selectQuery = "SELECT * FROM CARRERAS";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla CARRERAS:");
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                System.out.println("Nombre: " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla CARRERAS");
        }
    }

    public void eliminarCarreras() {
        String deleteQuery = "DELETE FROM CARRERAS";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar las carreras");
        }
    }
    
    public String[] obtenerCarreras() {
        String[] carreras = null;

        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM CARRERAS");

            // Obtener la cantidad de filas en el resultado
            rs.last();
            int rowCount = rs.getRow();

            // Crear un array con la cantidad de filas obtenidas
            carreras = new String[rowCount];

            // Volver al inicio del resultado
            rs.beforeFirst();

            // Llenar el array con los valores obtenidos
            int i = 0;
            while (rs.next()) {
                String carrera = rs.getString("nombre");
                carreras[i] = carrera;
                i++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carreras;
    }
}
