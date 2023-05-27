package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConecionOtras_materias {

    private Connection conn;

    public ConecionOtras_materias() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "OTRAS_MATERIAS" existe y crearla si no
            if (!tablaExiste("OTRAS_MATERIAS")) {
                crearTablaOtrasMaterias();
            }

            insertarMateria("Gestión Empresarial");
            insertarMateria("Contabilidad Financiera");
            insertarMateria("Economía Empresarial");
            insertarMateria("Dirección de Empresas");
            insertarMateria("Marketing Estratégico");
            insertarMateria("Negocios Internacionales");
            insertarMateria("Gestión de Recursos Humanos");
            insertarMateria("Finanzas Corporativas");
            insertarMateria("Gestión de Proyectos");
            insertarMateria("Comportamiento Organizacional");
            insertarMateria("Planificación Estratégica");
            insertarMateria("Marketing Digital");
            insertarMateria("Análisis Financiero");
            insertarMateria("Gestión de Operaciones");
            insertarMateria("Estrategias de Ventas");
            insertarMateria("Ética Empresarial");

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
                System.out.println("Conexión cerrada");
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

    private void crearTablaOtrasMaterias() throws SQLException {
        String createQuery = "CREATE TABLE OTRAS_MATERIAS ("
                + "nombre VARCHAR(100)"
                + ")";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla OTRAS_MATERIAS");
        }
    }

    private boolean materiaExiste(String nombre) {
        String selectQuery = "SELECT * FROM OTRAS_MATERIAS WHERE nombre = ?";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertarMateria(String nombre) {
        if (materiaExiste(nombre)) {
            return;
        }

        String insertQuery = "INSERT INTO OTRAS_MATERIAS (nombre) VALUES (?)";

        try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
            statement.setString(1, nombre);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar los datos en la tabla OTRAS_MATERIAS");
        }
    }

    public void mostrarMaterias() {
        String selectQuery = "SELECT * FROM OTRAS_MATERIAS";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla OTRAS_MATERIAS:");
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                System.out.println("Nombre: " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla OTRAS_MATERIAS");
        }
    }

    public void eliminarMaterias() {
        String deleteQuery = "DELETE FROM OTRAS_MATERIAS";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar las materias");
        }
    }

    public String[] obtenerMaterias() {
        String[] materias = null;

        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM OTRAS_MATERIAS");

            // Obtener la cantidad de filas en el resultado
            rs.last();
            int rowCount = rs.getRow();

            // Crear un array con la cantidad de filas obtenidas
            materias = new String[rowCount];

            // Volver al inicio del resultado
            rs.beforeFirst();

            // Llenar el array con los valores obtenidos
            int i = 0;
            while (rs.next()) {
                String materia = rs.getString("nombre");
                materias[i] = materia;
                i++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materias;
    }
}
