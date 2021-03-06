package com.neixar.sct.buscaLigas.Process;

import com.neixar.sct.buscaLigas.Files.ExportXLSLFile;
import com.neixar.sct.buscaLigas.Files.ListFiles;
import com.neixar.sct.buscaLigas.Files.SearchText;
import com.neixar.sct.buscaLigas.Record.Registro;

import java.util.Scanner;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.File;

public class Main {

	
	private static Scanner scanner;
	private static Collection<File> listadoArchivos = new ArrayList<File>(); //Listado de archivos del directorio a revisar
	private static HashMap<String,Registro> hashPrincipal = new HashMap<String,Registro>(); //Resultados

	public static void main(String[] args) {
		String pathOriginal = "";
		String archivoXLS = ""; 
		
		System.out.println("Analizador de Ligas\n\n");
				
		
		if(args.length == 2) {
			
			pathOriginal = args[0];
			archivoXLS = args[1];
			
		}else {
			System.out.print("Ingresa ruta: ");
			scanner = new Scanner(System.in);
			pathOriginal = scanner.nextLine();
			System.out.print("Nombre Archivo XLS: ");
			scanner = new Scanner(System.in);
			archivoXLS = scanner.nextLine();
			
		}
				
		File file = new File(pathOriginal);		
		ListFiles listFiles = new ListFiles();
		
		//Obtenemos de manera recursiva todos los archivos
		listFiles.getFiles(listadoArchivos, file);
		
		SearchText search = new SearchText();
		
		//Procesamos archivo por archivo, para buscar en su interior
		System.out.println("Total de archivos:" +  listadoArchivos.size());
		Iterator<File> itArchivos = listadoArchivos.iterator();
		
		while(itArchivos.hasNext()) {
			file = itArchivos.next();
			
			//Procesa archivo por archivo para localizar coincidencias
			//De hacerlo, lo acomoda en hashPrincipal
			search.processFile(file, hashPrincipal);
		}
		
		//queda pendiente explotar la colecci�n hash.
		System.out.println("Cantidad de registros: " +  hashPrincipal.size());
		
		String fileOutput =  pathOriginal + "/" + archivoXLS;
		ExportXLSLFile efile = new ExportXLSLFile(fileOutput,hashPrincipal,pathOriginal);
		efile.writeFile(archivoXLS);
		
		System.out.println("!Terminado el proceso!");
		
		
		
		
		
	}
	
}
