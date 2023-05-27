package Vista;

import Controlador.Crear_PDF;
import Modelo.ConexionMaterias;
import Modelo.ConecionOtras_materias;
import Modelo.ConexionConvalidacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Comparacion_Carreras extends JFrame {

    private JFrame frame;
    private JTable tabla1;
    private JTable tabla2;
    private JTable tablaConvalidacion;
    private JButton enviarButton;
    private JButton siguienteButton;
    private JButton cancelarButton;
    private JButton eliminarButton;

    private DefaultTableModel modeloConvalidacion;

    public Comparacion_Carreras() {
        setTitle("Comparación de Carreras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear el modelo de tabla para la tabla 1
        DefaultTableModel modeloTabla1 = new DefaultTableModel(new Object[][]{}, new String[]{"Materias"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No se permite la edición de celdas
            }
        };

        frame = this;
        
        // Crear la tabla 1 con el modelo de datos
        tabla1 = new JTable(modeloTabla1);

        // Agregar la tabla 1 a un JScrollPane
        JScrollPane scrollPane1 = new JScrollPane(tabla1);
        scrollPane1.setBounds(20, 20, 200, 200);
        add(scrollPane1);

        // Crear el modelo de tabla para la tabla 2
        DefaultTableModel modeloTabla2 = new DefaultTableModel(new Object[][]{}, new String[]{"Materias Solicitadas"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No se permite la edición de celdas
            }
        };

        // Crear la tabla 2 con el modelo de datos
        tabla2 = new JTable(modeloTabla2);

        // Agregar la tabla 2 a un JScrollPane
        JScrollPane scrollPane2 = new JScrollPane(tabla2);
        scrollPane2.setBounds(240, 20, 200, 200);
        add(scrollPane2);

        // Crear el botón Enviar
        enviarButton = new JButton("Enviar");
        enviarButton.setBounds(20, 240, 420, 30);
        add(enviarButton);

        // Crear el botón Siguiente
        siguienteButton = new JButton("Siguiente");
        siguienteButton.setBounds(20, 500, 200, 30);
        add(siguienteButton);

        // Crear el botón Cancelar
        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(240, 500, 200, 30);
        add(cancelarButton);

        // Crear el panel para la tabla de convalidación y el botón de eliminar
        JPanel panelConvalidacion = new JPanel();
        panelConvalidacion.setLayout(new BorderLayout());

        // Crear el modelo de tabla para la tabla de convalidación
        modeloConvalidacion = new DefaultTableModel(new Object[][]{}, new String[]{"Asignatura Cursada", "Asignatura de Convalidación"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No se permite la edición de celdas
            }
        };

        // Crear la tabla de convalidación con el modelo de datos
        tablaConvalidacion = new JTable(modeloConvalidacion) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);

                // Verificar si la fila ya ha sido enviada
                boolean enviado = false; // Cambiar esto según tu lógica

                if (enviado) {
                    // Si la fila ya ha sido enviada, mostrar una marca de verificación
                    JLabel label = new JLabel("\u2713"); // Marca de verificación
                    label.setForeground(Color.GREEN);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                }

                return component;
            }
        };

        // Agregar la tabla de convalidación a un JScrollPane
        JScrollPane scrollPaneConvalidacion = new JScrollPane(tablaConvalidacion);
        panelConvalidacion.add(scrollPaneConvalidacion, BorderLayout.CENTER);

        // Crear el botón Eliminar
        eliminarButton = new JButton("Eliminar materias seleccionadas");
        panelConvalidacion.add(eliminarButton, BorderLayout.SOUTH);

        // Agregar el panel de convalidación al JFrame
        panelConvalidacion.setBounds(20, 280, 420, 200);
        add(panelConvalidacion);

        setLayout(null);
        setSize(460, 580); // Tamaño del JFrame
        setLocationRelativeTo(null);
        setVisible(true);

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarDatos();
            }
        });

        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los datos de la última tabla y llamar al método para insertarlos en COMPARACION_CARRERAS
                String carrera1 = "";
                String carrera2 = "";

                int rowCount = modeloConvalidacion.getRowCount();
                if (rowCount > 0) {
                    carrera1 = modeloConvalidacion.getValueAt(rowCount - 1, 0).toString();
                    carrera2 = modeloConvalidacion.getValueAt(rowCount - 1, 1).toString();
                }

                // Insertar los datos en la tabla COMPARACION_CARRERAS
                ConexionConvalidacion conexionConvalidacion = new ConexionConvalidacion();
                conexionConvalidacion.eliminarComparacionCarreras();
                conexionConvalidacion.insertarDatosEnTablaComparacion(carrera1, carrera2);
                conexionConvalidacion.cerrarConexion();
                
                new Crear_PDF();
                dispose();
                
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para el botón Cancelar
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMateriasSeleccionadas();
            }
        });

        // Llenar la tabla 1 con los datos de la base de datos
        ConexionMaterias conexionMaterias = new ConexionMaterias();
        String[] materias = conexionMaterias.obtenerMaterias();
        for (String materia : materias) {
            modeloTabla1.addRow(new Object[]{materia});
        }

        // Llenar la tabla 2 con los datos de la nueva clase ConecionOtras_materias
        ConecionOtras_materias conection = new ConecionOtras_materias();
        String[] otrasMaterias = conection.obtenerMaterias();
        for (String materia : otrasMaterias) {
            modeloTabla2.addRow(new Object[]{materia});
        }
    }

    private void enviarDatos() {
        int filaSeleccionadaTabla1 = tabla1.getSelectedRow();
        int filaSeleccionadaTabla2 = tabla2.getSelectedRow();

        if (filaSeleccionadaTabla1 != -1 && filaSeleccionadaTabla2 != -1) {
            Object materiaTabla1 = tabla1.getValueAt(filaSeleccionadaTabla1, 0);
            Object materiaTabla2 = tabla2.getValueAt(filaSeleccionadaTabla2, 0);

            // Verificar si alguna materia ya ha sido enviada en la tabla de convalidación
            if (existeMateriaEnTablaConvalidacion(materiaTabla1, materiaTabla2)) {
                JOptionPane.showMessageDialog(null, "Al menos una de las materias ya existe en la tabla de convalidación", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Agregar los datos a la tabla de convalidación
                modeloConvalidacion.addRow(new Object[]{materiaTabla1, materiaTabla2});
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fila en cada tabla", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean existeMateriaEnTablaConvalidacion(Object materiaTabla1, Object materiaTabla2) {
        for (int i = 0; i < modeloConvalidacion.getRowCount(); i++) {
            Object materiaConvalidadaTabla1 = modeloConvalidacion.getValueAt(i, 0);
            Object materiaConvalidadaTabla2 = modeloConvalidacion.getValueAt(i, 1);

            if (materiaConvalidadaTabla1.equals(materiaTabla1) || materiaConvalidadaTabla2.equals(materiaTabla2)) {
                return true;
            }
        }
        return false;
    }

    private boolean verificarEnvio(Object materiaTabla1, Object materiaTabla2) {
        // Aquí debes implementar tu lógica para verificar si la fila ya ha sido enviada
        // Puedes utilizar los valores de las materias seleccionadas para hacer la verificación
        // Retorna true si ya ha sido enviada, false en caso contrario
        return false; // Cambiar esto según tu lógica
    }

    private void eliminarMateriasSeleccionadas() {
        int[] filasSeleccionadas = tablaConvalidacion.getSelectedRows();

        if (filasSeleccionadas.length > 0) {
            // Crear una lista para almacenar las filas seleccionadas (en orden inverso)
            ArrayList<Integer> filas = new ArrayList<>();
            for (int i = filasSeleccionadas.length - 1; i >= 0; i--) {
                filas.add(filasSeleccionadas[i]);
            }

            // Eliminar las filas seleccionadas de la tabla de convalidación
            for (int fila : filas) {
                modeloConvalidacion.removeRow(fila);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una fila para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
