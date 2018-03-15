package com.neixar.sct.buscaLigas.Files;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.neixar.sct.buscaLigas.Record.Registro;

public class ExportXLSLFile {

	private HashMap<String, Registro> hash;
	private FileOutputStream file;

	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	String[] headers = { "Servidor", "URL", "Archivos", "Ejemplos" };
	String pathOriginal;

	public ExportXLSLFile(String fileName, HashMap<String, Registro> hash, String pathOriginal) {
		this.pathOriginal = pathOriginal;
		this.hash = hash;
		try {
			file = new FileOutputStream(fileName.trim() + ".xls");
		} catch (FileNotFoundException ex) {
			System.out.println("No se puede crear el archivo: " + fileName + ".xls");
			ex.printStackTrace();
		}
	}

	public void writeFile() {

		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		workbook.setSheetName(0, "Ligas");

		String dataBody;
		String exampleBody;
		try {

			// Estilo del encabezado
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

			// Crear encabezado
			int row = 0;
			HSSFRow headerRow = sheet.createRow(row++);
			int col = 0;
			for (String header : headers) {
				HSSFCell cell = headerRow.createCell(col++);
				cell.setCellStyle(headerStyle); // Header en Bold
				cell.setCellValue(header);
			}

			// Datos de la Hoja Excel
			for (Map.Entry<String, Registro> entry : hash.entrySet()) {

				dataBody = "";
				exampleBody = "";

				Registro registro = entry.getValue();
				ArrayList<String> urls = registro.getUrl();
				ArrayList<String> archivos = registro.getArchivos();
				ArrayList<String> ejemplos = registro.getEjemplos();

				col = 0;
				// Conformaci�n del nuevo rengl�n (S�lo si contiene ejemplos)
				// Cuarta columna: Ejemplos
				exampleBody = "";
				for (String ejemplo : ejemplos) {
					ejemplo = ejemplo.trim();
					if (ejemplo.startsWith("*") || ejemplo.startsWith("//") || ejemplo.startsWith("/*"))
						continue;

					// Que cada l�nea de ejemplo, no exceda 300 caracteres
					if (ejemplo.length() > 300)
						ejemplo = ejemplo.substring(0, 299);

					exampleBody += ejemplo + "\n";
				}

				if (exampleBody.trim().equals(""))
					continue; // Si no tiene ejemplos

				HSSFRow rowdataXLS = sheet.createRow(row++);
				HSSFCell cell = rowdataXLS.createCell(col++);
				cell.setCellValue(entry.getKey()); // Nombre del Servidor (columna 0)

				// Segunda columna: URLs
				for (String url : urls)
					dataBody += url.trim() + "\n";

				cell = rowdataXLS.createCell(col++);

				dataBody = formatCell(dataBody);

				cell.setCellValue(dataBody);

				// Tercera columna: Archivo
				dataBody = "";
				for (String archivo : archivos) {
					//Substraemos a "archivo" el path original (pathOriginal)
					archivo = archivo.substring(pathOriginal.length(), archivo.length()-1);					
					dataBody += archivo.trim() + "\n";
				}
				cell = rowdataXLS.createCell(col++);

				dataBody = formatCell(dataBody);

				cell.setCellValue(dataBody);

				// Registramos la cuarta columna (Ejemplos)
				cell = rowdataXLS.createCell(col++);

				// Limitamos el tama�o del contenido en la celda de ejemplo.
				exampleBody = formatCell(exampleBody);

				cell.setCellValue(exampleBody);

			}
			workbook.write(file);

			// Borramos los renglones que carezcan de ejemplos
			// delRowBlank(sheet);

			workbook.close();

		} catch (IOException ex) {
			System.out.println("Error::ExportFile.write:");
			ex.printStackTrace();
		}
	}

	String formatCell(String cell) {

		if (cell.endsWith("\n"))
			cell = cell.substring(0, cell.length() - 1);

		if (cell.length() > 32766)
			cell = cell.substring(0, 32766);

		return cell;

	}

}
