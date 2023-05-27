package Controlador;

import Modelo.ConexionSolicitud;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Crear_PDF {

    private ConexionSolicitud conexionSolicitud;

    public Crear_PDF() {
        conexionSolicitud = new ConexionSolicitud();
        generarPDF();
    }

    public void generarPDF() {
        // Obtener los números de control de la tabla Solicitudes
        String numerosControl = conexionSolicitud.obtenerNumerosControl();

        // Obtener el tecnológico de procedencia de la tabla Solicitudes
        String tecnologicoProcedencia = conexionSolicitud.obtenerTecnologicoProcedencia();

        // Modificar el número de control para usarlo como nombre del archivo PDF
        String numeroControlValido = numerosControl.replaceAll("[^a-zA-Z0-9_-]", "_");

        // Generar el PDF con los números de control y el tecnológico de procedencia
        Document document = new Document();

        try {
            // Establecer la ruta del proyecto
            String rutaProyecto = System.getProperty("user.dir");

            // Establecer la ruta y el nombre del archivo PDF a generar
            String rutaArchivo = rutaProyecto + "/" + numeroControlValido + ".pdf";

            // Crear el objeto PdfWriter para escribir en el archivo PDF
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));

            // Agregar un evento para generar el contenido del encabezado en cada página
            writer.setPageEvent((PdfPageEvent) new EncabezadoPDF());

            // Abrir el documento
            document.open();

            // Agregar el contenido al documento
            Paragraph paragraph = new Paragraph("Número de control: ");
            document.add(paragraph);
            document.add(new Paragraph(numerosControl));

            // Agregar la tabla con el tecnológico de procedencia
            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = new PdfPCell(new Paragraph("Tecnológico de procedencia"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(tecnologicoProcedencia));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            document.add(table);

            // Agregar una tabla separada para las materias
            PdfPTable segundaTabla = new PdfPTable(3);

            PdfPCell encabezado1 = new PdfPCell(new Paragraph("Asignatura cursada"));
            encabezado1.setHorizontalAlignment(Element.ALIGN_CENTER);
            segundaTabla.addCell(encabezado1);

            PdfPCell encabezado2 = new PdfPCell(new Paragraph("Calificación"));
            encabezado2.setHorizontalAlignment(Element.ALIGN_CENTER);
            segundaTabla.addCell(encabezado2);

            PdfPCell encabezado3 = new PdfPCell(new Paragraph("Convalidación"));
            encabezado3.setHorizontalAlignment(Element.ALIGN_CENTER);
            segundaTabla.addCell(encabezado3);

            // Obtener las materias
            String[] materias = obtenerMaterias();

            // Llenar la tabla con las materias
            for (String materia : materias) {
                PdfPCell celdaMateria = new PdfPCell(new Paragraph(materia));
                segundaTabla.addCell(celdaMateria);

                // Dejar vacías la segunda y tercera columna
                PdfPCell celdaVacia1 = new PdfPCell(new Paragraph(""));
                segundaTabla.addCell(celdaVacia1);

                PdfPCell celdaVacia2 = new PdfPCell(new Paragraph(""));
                segundaTabla.addCell(celdaVacia2);
            }

            document.add(segundaTabla);

            // Cerrar el documento
            document.close();

            System.out.println("PDF generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al generar el PDF.");
        }

        // Cerrar la conexión a la base de datos
        conexionSolicitud.cerrarConexion();
    }

    private String[] obtenerMaterias() {
        String[] materias = null;

        try {
            Statement stmt = conexionSolicitud.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM MATERIAS");

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

    private class EncabezadoPDF extends PdfPageEventHelper {

        private Random random = new Random();
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        private BaseFont baseFont;

        public EncabezadoPDF() {
            try {
                // Cargar la fuente BaseFont predeterminada (puedes cambiarla a la fuente que desees)
                baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();

            // Generar el folio aleatorio
            int folio = random.nextInt(100000) + 1;
            String folioTexto = String.format("Folio: %06d", folio);

            // Obtener la fecha actual
            String fechaActual = dateFormat.format(new Date());

            // Establecer la posición del folio y la fecha
            float xPosFolio = (document.left() + document.right()) / 2;
            float yPos = document.top() + 10;
            float xPosFecha = document.right() - 60;

            // Escribir el folio en el contenido PDF
            cb.beginText();
            cb.setFontAndSize(baseFont, 10);
            cb.showTextAligned(Element.ALIGN_CENTER, folioTexto, xPosFolio, yPos, 0);
            cb.endText();

            // Escribir la fecha en el contenido PDF
            cb.beginText();
            cb.setFontAndSize(baseFont, 10);
            cb.showTextAligned(Element.ALIGN_RIGHT, fechaActual, xPosFecha, yPos, 0);
            cb.endText();
        }
    }
}
