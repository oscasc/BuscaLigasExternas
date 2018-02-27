package com.neixar.sct.buscaLigas.Process;

import com.neixar.sct.buscaLigas.Files.ListFiles;

import java.util.Scanner;
import java.util.Collection;
import java.util.Iterator;
import java.io.File;

public class Main {

	private static Scanner scanner;

	public static void main(String[] args) {
		String pathOriginal = "";
		
		if(args.length >= 1) {
			pathOriginal = args[0];
			
		}else {
			System.out.print("Ingresa ruta: ");
			scanner = new Scanner(System.in);
			pathOriginal = scanner.nextLine();
		}
				
		File file = new File(pathOriginal);		
		ListFiles listFiles = new ListFiles();
		
		//print files inside:
		listFiles.getFiles(file);
		
		
		
		
		
	}
	
}
