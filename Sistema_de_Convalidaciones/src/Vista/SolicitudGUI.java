package Vista;

import Modelo.ConexionCarreras;
import Modelo.ConexionSemestre;
import Modelo.ConexionSolicitud;
import Modelo.ConexionTecnologicos;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.*;

public class SolicitudGUI extends JFrame {

    private ConexionTecnologicos conexionTecnologico;
    private ConexionSemestre conexionSemestre;
    private ConexionCarreras conexionCarreras;

    private JFrame frame;
    private JLabel institutoLabel, noControlLabel, semestreLabel, carreraActualLabel, carreraSolicitadaLabel,
            celularLabel, emailLabel, claveCarreraLabel;
    private JTextField noControlTextField, celularTextField, emailTextField, claveCarreraTextField;
    private JComboBox<Integer> semestreComboBox;
    private JComboBox<String> TecnologicoProcedenciaComboBox, carreraActualComboBox, carreraSolicitadaComboBox;
    private JButton enviarButton, cancelarButton;

    public SolicitudGUI() {
        setTitle("Solicitud de Convalidación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        conexionTecnologico = new ConexionTecnologicos();
        conexionSemestre = new ConexionSemestre();
        conexionCarreras = new ConexionCarreras();

        frame = this;
        institutoLabel = new JLabel("Instituto Tecnológico de procedencia:");
        noControlLabel = new JLabel("No. de Control:");
        semestreLabel = new JLabel("         Semestre:");
        carreraActualLabel = new JLabel("Carrera que curso el interesado:");
        carreraSolicitadaLabel = new JLabel("Carrera que solicito el interesado:");
        claveCarreraLabel = new JLabel("Clave del plan de estudios:");
        celularLabel = new JLabel("Teléfono CELULAR del solicitante:");
        emailLabel = new JLabel("Correo electrónico del solicitante:");

        noControlTextField = new JTextField(6);
        claveCarreraTextField = new JTextField(10);
        celularTextField = new JTextField(7);
        emailTextField = new JTextField(25);

        // Obtener los valores de la tabla TECNOLOGICOS y guardarlos en un array
        String[] tecnologicos = conexionTecnologico.obtenerTecnologicos();
        String[] carreras = conexionCarreras.obtenerCarreras();
        int[] semestres = conexionSemestre.obtenerSemestre();

        // Crear un arreglo de objetos Integer para los valores de los semestres
        Integer[] semestresObj = new Integer[semestres.length];
        for (int i = 0; i < semestres.length; i++) {
            semestresObj[i] = semestres[i];
        }

        // Crear el JComboBox con los valores obtenidos
        TecnologicoProcedenciaComboBox = new JComboBox<>(tecnologicos);
        carreraActualComboBox = new JComboBox<>(carreras);
        carreraSolicitadaComboBox = new JComboBox<>(carreras);
        semestreComboBox = new JComboBox<>(semestresObj);

        // Cerrar la conexión
        conexionTecnologico.cerrarConexion();
        conexionSemestre.cerrarConexion();
        conexionCarreras.cerrarConexion();

        enviarButton = new JButton("Enviar");
        cancelarButton = new JButton("Cancelar");

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5); // margen de 5 en todos los lados
        constraints.anchor = GridBagConstraints.WEST;

        add(institutoLabel, constraints);

        JPanel noControlSemestrePanel = new JPanel(new GridLayout(1, 4, 10, 0));
        noControlSemestrePanel.add(noControlLabel);
        noControlSemestrePanel.add(noControlTextField);
        noControlSemestrePanel.add(semestreLabel);
        noControlSemestrePanel.add(semestreComboBox);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(noControlSemestrePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridy++;
        add(carreraActualLabel, constraints);

        constraints.gridy++;
        add(carreraSolicitadaLabel, constraints);

        constraints.gridy++;
        add(claveCarreraLabel, constraints);

        constraints.gridy++;
        add(celularLabel, constraints);

        constraints.gridy++;
        add(emailLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 0, 5, 5); // margen de 5 a la derecha, 0 arriba y abajo, 5 a la izquierda
        constraints.anchor = GridBagConstraints.WEST;

        add(TecnologicoProcedenciaComboBox, constraints);

        constraints.gridy = 3;
        add(carreraActualComboBox, constraints);

        constraints.gridy++;
        add(carreraSolicitadaComboBox, constraints);

        constraints.gridy++;
        add(claveCarreraTextField, constraints);

        constraints.gridy++;
        add(celularTextField, constraints);

        constraints.gridy++;
        add(emailTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(15, 0, 5, 0); // margen de 15 arriba, 5 a la derecha y a la izquierda, 0 abajo
        constraints.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(enviarButton);
        enviarButton.addActionListener(new HandlerEnviar());

        buttonPanel.add(cancelarButton);
//        cancelarButton.addActionListener(new HandlerCancelar());

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets.top = 15;

        constraints.gridy++;
        constraints.insets = new Insets(5, 100, 10, 0); // margen de 5 arriba y abajo, 0 a la derecha y a la izquierda
        add(buttonPanel, constraints);
        pack(); // Ajustar el tamaño del frame a los componentes
        setLocationRelativeTo(null); // Centrar el frame en la pantalla
        setVisible(true); // Mostrar el frame
    }

    private class HandlerEnviar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ConexionSolicitud coneSolicitud = new ConexionSolicitud();

            coneSolicitud.eliminarSolicitudes();

            String tecnologicoProcedencia = TecnologicoProcedenciaComboBox.getSelectedItem().toString();
            String noControl = noControlTextField.getText();
            int semestre = (int) semestreComboBox.getSelectedItem();
            String carreraActual = carreraActualComboBox.getSelectedItem().toString();
            String carreraSolicitada = carreraSolicitadaComboBox.getSelectedItem().toString();
            String claveCarrera = claveCarreraTextField.getText();
            String celular = celularTextField.getText();
            String email = emailTextField.getText();

            if (TecnologicoProcedenciaComboBox.getSelectedItem() == null
                    || noControlTextField.getText().isEmpty()
                    || semestreComboBox.getSelectedItem() == null
                    || carreraActualComboBox.getSelectedItem() == null
                    || carreraSolicitadaComboBox.getSelectedItem() == null
                    || claveCarreraTextField.getText().isEmpty()
                    || celularTextField.getText().isEmpty()
                    || emailTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debes llenar todos los campos.");
                return;
            }

            // Verificar si el número de control ya hizo una solicitud
            if (coneSolicitud.solicitudExistente(noControl)) {
                JOptionPane.showMessageDialog(null, "El número de control ya ha realizado una solicitud.");
                return;
            }
            coneSolicitud.insertarSolicitud(tecnologicoProcedencia, noControl, semestre,
                    carreraActual, carreraSolicitada, claveCarrera,
                    celular, email);

            new SeguimientoAcademicoGUI();
            frame.dispose();
            JOptionPane.showMessageDialog(null, "Ingresa tus calificaciones que sacaste en cada materia\n"
                    + "En caso de ser una materia que aún no cursas, déjala como NA");
        }
    }

}
