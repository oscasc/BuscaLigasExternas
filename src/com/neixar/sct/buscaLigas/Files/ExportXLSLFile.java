package com.neixar.sct.buscaLigas.Files;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import java.util.ArrayList;
import com.neixar.sct.buscaLigas.Record.Registro;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExportXLSLFile {

	private HashMap<String, Registro> hash;
	private FileOutputStream file;
	
	private  HSSFWorkbook workbook;
	private HSSFSheet sheet;
	String[] headers = {"Servidor", "URL", "Archivos", "Ejemplos"};
	

	public ExportXLSLFile(String fileName, HashMap<String, Registro> hash) {
		this.hash = hash;
		try {
		file = new FileOutputStream(fileName.trim() + ".xls");
		}catch(FileNotFoundException ex){
			System.out.println("No se puede crear el archivo: " +  fileName + ".xls");
			ex.printStackTrace();
		}
	}

	public void writeFile() {
		
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		workbook.setSheetName(0, "Ligas");
		
		
		
		String dataBody;
		try {


			
			//Estilo del encabezado
			CellStyle headerStyle = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
			
			//Crear encabezado
			int row = 0;
			HSSFRow headerRow = sheet.createRow(row++);
			int col=0;
			for(String header: headers) {
				HSSFCell cell = headerRow.createCell(col++);
				cell.setCellStyle(headerStyle); //Header en Bold
				cell.setCellValue(header);
			}
			
			//Datos de la Hoja Excel
			for (Map.Entry<String, Registro> entry : hash.entrySet()) {
				
				Registro registro = entry.getValue();
				ArrayList<String> urls = registro.getUrl();
				ArrayList<String> archivos = registro.getArchivos();
				ArrayList<String> ejemplos = registro.getEjemplos();
				
				col = 0;
				//Conformación del nuevo renglón
				HSSFRow rowdataXLS = sheet.createRow(row++);
				HSSFCell cell = rowdataXLS.createCell(col++);
				cell.setCellValue(entry.getKey()); //Nombre del Servidor (columna 0)
				
				//Segunda columna: URLs
				dataBody = "";
				for(String url: urls) 
					dataBody += url + "\n";
				cell = rowdataXLS.createCell(col++);
				cell.setCellValue(dataBody);
				
				//Tercera columna: Archivo
				dataBody = "";
				for (String archivo : archivos) 					
					dataBody += archivo + "\n";			
				cell = rowdataXLS.createCell(col++);
				cell.setCellValue(dataBody);
				
				//Cuarta columna: Ejemplos
				dataBody = "";
				for (String ejemplo : ejemplos) {
					if (ejemplo.trim().startsWith("*") || ejemplo.trim().startsWith("//"))
						continue;

					if (ejemplo.length() > 500)
						ejemplo = ejemplo.substring(0, 499);

					dataBody += ejemplo + "\n";
				}
				cell = rowdataXLS.createCell(col++);
				cell.setCellValue(dataBody);

				

			}
			workbook.write(file);
			workbook.close();

		} catch (IOException ex) {
			System.out.println("Error::ExportFile.write:");
			ex.printStackTrace();
		}
	}

}
