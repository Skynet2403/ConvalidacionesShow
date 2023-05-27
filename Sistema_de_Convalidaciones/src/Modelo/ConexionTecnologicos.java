package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionTecnologicos {
    private Connection conn;

    public ConexionTecnologicos() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "TECNOLOGICOS" existe y crearla si no
            if (!tablaExiste("TECNOLOGICOS")) {
                crearTablaTecnologicos();
            }

            insertarTecnologico("Instituto Tecnológico de León");
            insertarTecnologico("Instituto Tecnológico de Morelia");
            insertarTecnologico("Instituto Tecnológico de La Piedad");
            insertarTecnologico("Instituto Tecnológico de Mazatlan");
            insertarTecnologico("Instituto Tecnológico de Toluca");
            insertarTecnologico("Instituto Tecnológico de Mexico");
            
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
    
    public void insertarTecnologico(String nombre) {
        String insertQuery = "INSERT INTO TECNOLOGICOS (nombre) VALUES (?)";

        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            // Verificar si la clave primaria ya existe
            if (existeTecnologico(nombre)) {
                return; // No se realiza la inserción
            }

            statement.setString(1, nombre);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar los datos en la tabla TECNOLOGICOS");
        }
    }
    
    public void mostrarTecnologicos() {
        String selectQuery = "SELECT * FROM TECNOLOGICOS";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();
            
            System.out.println("Valores en la tabla TECNOLOGICOS:");
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                System.out.println(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla TECNOLOGICOS");
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

    private void crearTablaTecnologicos() throws SQLException {
        String createQuery = "CREATE TABLE TECNOLOGICOS (nombre VARCHAR(100) PRIMARY KEY)";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla TECNOLOGICOS");
        }
    }
    
    private boolean existeTecnologico(String nombre) throws SQLException {
        boolean existe = false;
        String query = "SELECT 1 FROM TECNOLOGICOS WHERE nombre = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, nombre);
            existe = statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }
    
    public String[] obtenerTecnologicos() {
        String[] tecnologicos = null;

        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM TECNOLOGICOS");

            // Obtener la cantidad de filas en el resultado
            rs.last();
            int rowCount = rs.getRow();

            // Crear un array con la cantidad de filas obtenidas
            tecnologicos = new String[rowCount];

            // Volver al inicio del resultado
            rs.beforeFirst();

            // Llenar el array con los valores obtenidos
            int i = 0;
            while (rs.next()) {
                String tecnologico = rs.getString("nombre");
                tecnologicos[i] = tecnologico;
                i++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tecnologicos;
    }
    
    public void eliminarTecnologicos() {
        String deleteQuery = "DELETE FROM TECNOLOGICOS";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar los tecnológicos");
        }
    }
}
