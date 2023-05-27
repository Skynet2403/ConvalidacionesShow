package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ConexionSemestre {

    private Connection conn;

    public ConexionSemestre() {
        try {
            // Establecer la URL de conexión
            String url = "jdbc:derby://localhost:1527/shopmedb;create=true";

            // Establecer las credenciales de conexión si es necesario
            String usuario = "Convalidacion";
            String contraseña = "itl123";

            // Establecer la conexión
            conn = DriverManager.getConnection(url, usuario, contraseña);

            // Verificar si la tabla "SEMESTRES" existe y crearla si no
            if (!tablaExiste("SEMESTRES")) {
                crearTablaSemestres();
            }

            insertarSemestre(1);
            insertarSemestre(2);
            insertarSemestre(3);
            insertarSemestre(4);
            insertarSemestre(5);
            insertarSemestre(6);
            insertarSemestre(7);
            insertarSemestre(8);
            insertarSemestre(9);
            insertarSemestre(10);
            insertarSemestre(11);
            insertarSemestre(12);

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

    private void crearTablaSemestres() throws SQLException {
        String createQuery = "CREATE TABLE SEMESTRES ("
                + "numero INT"
                + ")";

        try (PreparedStatement statement = conn.prepareStatement(createQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al crear la tabla SEMESTRES");
        }
    }

    public void insertarSemestre(int numero) {
        if (!semestreExiste(numero)) {
            String insertQuery = "INSERT INTO SEMESTRES (numero) VALUES (?)";

            try (PreparedStatement statement = conn.prepareStatement(insertQuery)) {
                statement.setInt(1, numero);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar el semestre");
            }
        } else {
        }
    }

    private boolean semestreExiste(int numero) {
        String selectQuery = "SELECT * FROM SEMESTRES WHERE numero = ?";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            statement.setInt(1, numero);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void mostrarSemestres() {
        String selectQuery = "SELECT * FROM SEMESTRES";

        try (PreparedStatement statement = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Valores en la tabla SEMESTRES:");
            while (resultSet.next()) {
                int numero = resultSet.getInt("numero");
                System.out.println("Semestre: " + numero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los datos de la tabla SEMESTRES");
        }
    }

    public void eliminarSemestres() {
        String deleteQuery = "DELETE FROM SEMESTRES";

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar los semestres");
        }
    }
    
    public int[] obtenerSemestre() {
        int[] semestres = null;

        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT numero FROM SEMESTRES");

            // Obtener la cantidad de filas en el resultado
            rs.last();
            int rowCount = rs.getRow();

            // Crear un array con la cantidad de filas obtenidas
            semestres = new int[rowCount];

            // Volver al inicio del resultado
            rs.beforeFirst();

            // Llenar el array con los valores obtenidos
            int i = 0;
            while (rs.next()) {
                int semestre = rs.getInt("numero");
                semestres[i] = semestre;
                i++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return semestres;
    }
}
