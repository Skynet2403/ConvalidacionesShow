package Vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Modelo.ConexionCalificaciones;
import Modelo.ConexionMaterias;

public class SeguimientoAcademicoGUI extends JFrame {

    private JTable tabla;
    private JButton enviarButton;
    private JButton cancelarButton;
    private JFrame frame;
    private ConexionCalificaciones conexionCalificaciones;

    public SeguimientoAcademicoGUI() {
        setTitle("Seguimiento Académico");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        frame = this;

        // Obtener las materias desde la clase ConexionMaterias
        ConexionMaterias conexionCalificaciones= new ConexionMaterias();
        String[] materias = conexionCalificaciones.obtenerMaterias();
        
        conexionCalificaciones.cerrarConexion();

        // Títulos de las columnas
        String[] titulos = {"Materia", "Calificación"};

        // Crear el modelo de tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Permitir la edición solo en la columna de calificación
                return column == 1;
            }
        };

        // Llenar el modelo de tabla con los datos de las materias
        for (String materia : materias) {
            modeloTabla.addRow(new Object[]{materia, "NA"});
        }

        // Crear el renderizador de celdas para centrar los valores de la columna "Calificación"
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Crear la tabla con el modelo de datos
        tabla = new JTable(modeloTabla);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // Agregar la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Crear los botones
        enviarButton = new JButton("Enviar");
        cancelarButton = new JButton("Cancelar");

        // Crear un panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enviarButton);
        buttonPanel.add(cancelarButton);

        // Establecer el layout del frame como GridBagLayout
        setLayout(new GridBagLayout());

        // Establecer las restricciones para el JScrollPane
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.weightx = 1.0;
        scrollConstraints.weighty = 1.0;
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrollPane, scrollConstraints);

        // Establecer las restricciones para el panel de botones
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(buttonPanel, buttonConstraints);

        // Agregar el ActionListener al botón "Enviar"
        enviarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtener los datos de la tabla
                Object[][] datos = new Object[modeloTabla.getRowCount()][2];
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                        datos[i][j] = modeloTabla.getValueAt(i, j);
                    }
                }

                // Insertar los datos en la base de datos
                ConexionCalificaciones conexionCalificaciones = new ConexionCalificaciones();
                
                for (int i = 0; i < datos.length; i++) {
                    String materia = (String) datos[i][0];
                    String calificacion = (String) datos[i][1];
                        conexionCalificaciones.insertarCalificacion(materia, calificacion);
                }
                
                conexionCalificaciones.eliminarTodasCalificaciones();
                
                conexionCalificaciones.cerrarConexion();

                new Comparacion_Carreras();
                frame.dispose();
            }
        });

        // Agregar el ActionListener al botón "Cancelar"
        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                conexionCalificaciones.cerrarConexion();
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }
}

