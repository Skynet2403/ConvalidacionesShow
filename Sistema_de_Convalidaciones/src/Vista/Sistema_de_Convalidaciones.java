 package Vista;

import Modelo.*;

/**
 *
 * @author Jonathan de Jesus Perez Becerra
 */
public class Sistema_de_Convalidaciones {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ConexionTecnologicos conCarreras = new ConexionTecnologicos();
//        ConexionMaterias conMaterias = new ConexionMaterias();
//        ConexionCarreras conCarreras = new ConexionCarreras();
        ConexionSemestre conSemestre = new ConexionSemestre();
        
        new SolicitudGUI();
    }
    
}
