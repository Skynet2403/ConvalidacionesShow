/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_de_convalidaciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 *
 * @author Jonathan de Jesus Perez Becerra
 */
public class Sistema_de_Convalidaciones {

    private static Connection con;
    private static Statement sentencia;
    public static ResultSet resultados;
     public static ResultSet resultadosTemp;
    private static String consulta;
    private static ResultSetMetaData metada;
    private static String[] nameColumns;
    public static String [][] rowData;
    private static int rows;
    private static String str; 
    private String nameT, nameBD;
    
    public Sistema_de_Convalidaciones() {
  
        consulta = "select * from Tecnologicos"; //equis la tabla a consultar
        controlador();
//        makeConsulta();
//        imprimeConsulta();
        
    }

    void controlador() {
        String conexionurl = "jdbc:sqlserver://JONATHAN_PC\\SQLEXPRESS:1433;databaseName=Convalidacion;"
                + "user=Conva;"
                + "password=ITL123;"
                + "trustServerCertificate=true;";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(conexionurl);
            System.out.println("Conexion exitosa");
        } catch (Exception ex) {
            System.out.println("error de conexion");
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
       new Sistema_de_Convalidaciones();
    }
    
}
