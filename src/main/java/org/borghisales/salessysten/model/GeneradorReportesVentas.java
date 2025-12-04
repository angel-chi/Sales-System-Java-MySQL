package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
// Importaciones duplicadas eliminadas
// import org.apache.pdfbox.pdmodel.PDDocument;
// import org.apache.pdfbox.pdmodel.PDPage;
// import org.apache.pdfbox.pdmodel.PDPageContentStream;
// import org.apache.pdfbox.pdmodel.font.PDType1Font;

// import javafx.collections.ObservableList; // Ya importado
import org.borghisales.salessysten.controllers.MenuController;

import java.awt.Color;
// import java.io.IOException; // Ya importado
// import java.time.LocalDate; // Ya importado

import java.awt.*;
// import java.io.IOException; // Ya importado

// Nombre de clase cambiado a español
public class GeneradorReportesVentas {

    public static void generateCSVReport(ObservableList<Venta> listaVentas, String outputPath) { // Tipo de lista y parámetro cambiado
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Escribir encabezados de columna en español
            writer.append("ID Venta,ID Cliente,ID Vendedor,Número de Venta,Fecha de Venta,Total,Estado\n");

            // Escribir datos de ventas
            for (Venta venta : listaVentas) { // Tipo de objeto y nombre de variable cambiado
                writer.append(String.valueOf(venta.idVenta())).append(","); // Nombres de métodos de entidad cambiados
                writer.append(String.valueOf(venta.idCliente())).append(","); // Nombres de métodos de entidad cambiados
                writer.append(String.valueOf(venta.idVendedor())).append(","); // Nombres de métodos de entidad cambiados
                writer.append(venta.numeroDeVenta()).append(","); // Nombres de métodos de entidad cambiados
                writer.append(venta.fechaDeVenta().toString()).append(","); // Nombres de métodos de entidad cambiados
                writer.append(String.valueOf(venta.total())).append(","); // Nombres de métodos de entidad cambiados
                writer.append(venta.estado().toString()).append("\n"); // Nombres de métodos de entidad cambiados
            }

            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Reporte CSV generado correctamente en " + outputPath); // Mensaje traducido

        } catch (IOException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al generar reporte CSV: " + e.getMessage()); // Mensaje traducido
        }
    }
    public static void generateExcelReport(ObservableList<Venta> listaVentas, String outputPath) { // Tipo de lista y parámetro cambiado
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Ventas"); // Nombre de hoja traducido

            // Crear encabezados de columna en español
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID Venta", "ID Cliente", "ID Vendedor", "Número de Venta", "Fecha de Venta", "Total", "Estado"}; // Nombres de columna traducidos
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Agregar datos de ventas
            int rowNum = 1;
            for (Venta venta : listaVentas) { // Tipo de objeto y nombre de variable cambiado
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(venta.idVenta()); // Nombres de métodos de entidad cambiados
                row.createCell(1).setCellValue(venta.idCliente()); // Nombres de métodos de entidad cambiados
                row.createCell(2).setCellValue(venta.idVendedor()); // Nombres de métodos de entidad cambiados
                row.createCell(3).setCellValue(venta.numeroDeVenta()); // Nombres de métodos de entidad cambiados
                row.createCell(4).setCellValue(venta.fechaDeVenta().toString()); // Nombres de métodos de entidad cambiados
                row.createCell(5).setCellValue(venta.total()); // Nombres de métodos de entidad cambiados
                row.createCell(6).setCellValue(venta.estado().toString()); // Nombres de métodos de entidad cambiados
            }

            // Ajustar el ancho de las columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir el libro de trabajo en un archivo
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
            }

            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Reporte EXCEL generado correctamente en " + outputPath); // Mensaje traducido

        } catch (IOException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al generar reporte EXCEL: " + e.getMessage()); // Mensaje traducido
        }
    }
    public static void generatePDFReport(ObservableList<Venta> listaVentas, String outputPath) { // Tipo de lista y parámetro cambiado
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20;


            // Obtener los nombres de las columnas en español
            String[] columnNames = { "ID Venta", "ID Cliente", "ID Vendedor", "Número de Venta", "Fecha de Venta", "Total",
                    "Estado" }; // Nombres de columna traducidos

            // Calcular los anchos de columna basados en los nombres de columna más largos
            float[] columnWidths = calculateColumnWidths(columnNames, PDType1Font.HELVETICA, 12);

            // Dibujar encabezados de columna
            drawRow(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths, Color.LIGHT_GRAY, true,
                    columnNames);
            yPosition -= rowHeight;

            // Dibujar filas con los datos de listaVentas
            for (Venta venta : listaVentas) { // Tipo de objeto y nombre de variable cambiado
                drawRow(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths, Color.WHITE, false,
                        String.valueOf(venta.idVenta()), String.valueOf(venta.idCliente()), // Nombres de métodos de entidad cambiados
                        String.valueOf(venta.idVendedor()), venta.numeroDeVenta(), venta.fechaDeVenta().toString(), // Nombres de métodos de entidad cambiados
                        String.valueOf(venta.total()), venta.estado().toString()); // Nombres de métodos de entidad cambiados
                yPosition -= rowHeight;
            }

            contentStream.close();

            document.save(outputPath);
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Reporte PDF generado correctamente en " + outputPath); // Mensaje traducido

        } catch (IOException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al generar reporte PDF: " + e.getMessage()); // Mensaje traducido

        }
    }

    private static float[] calculateColumnWidths(String[] columnNames, PDType1Font font, int fontSize)
            throws IOException {
        float[] columnWidths = new float[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            float textWidth = font.getStringWidth(columnNames[i]) / 1000 * fontSize;
            columnWidths[i] = textWidth + 2 * 5; // Add some margin
        }
        return columnWidths;
    }

    private static void drawRow(PDPageContentStream contentStream, float x, float y, float width, float rowHeight,
                                float[] columnWidths, Color backgroundColor, boolean isHeader, String... data) throws IOException {
        contentStream.setNonStrokingColor(backgroundColor);
        contentStream.fillRect(x, y, width, rowHeight);
        contentStream.setNonStrokingColor(Color.BLACK);
        float cellMargin = 5;

        float nextX = x;
        for (int i = 0; i < data.length; i++) {
            String text = data[i];
            float cellWidth = columnWidths[i];

            float textX = nextX + cellMargin;
            float textY = y + rowHeight / 2 - 12 / 2; // Font size 12
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(text);
            contentStream.endText();

            nextX += cellWidth;

            // Agregar un espacio entre las columnas
            if (i < data.length - 1) {
                nextX += 10; // Espacio entre columnas
            }
        }
    }

}
