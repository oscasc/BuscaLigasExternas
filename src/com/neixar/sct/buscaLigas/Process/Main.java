package com.neixar.sct.buscaLigas.Process;

import com.neixar.sct.buscaLigas.Files.GetAllFiles;
import java.util.Scanner;
import java.util.Collection;
import java.util.Iterator;
import java.io.File;

public class Main {

	private static Scanner scanner;

	public static void main(String[] args) {
		String pathOriginal = "";
		GetAllFiles getAllFiles;
		if(args.length >= 1) {
			pathOriginal = args[0];
			
		}else {
			System.out.print("Ingresa ruta: ");
			scanner = new Scanner(System.in);
			pathOriginal = scanner.nextLine();
		}
		
		getAllFiles = new GetAllFiles(pathOriginal);
		
		Collection<File> files = getAllFiles.getFiles();
		Iterator<File> iterator = files.iterator();
		while(iterator.hasNext()) {
			File file = iterator.next();
			String path = file.getAbsolutePath();
			String strFile = file.getName();
			
			//System.out.println("path: " +  path + " file: " + strFile);
		}
		
		System.out.println("End");
		
		
	}
	
}
