package com.neixar.sct.buscaLigas.Files;

import java.util.Collection;
import java.util.ArrayList;
import java.io.File;

/*
 * Clase que retorna listado de todos los archivos debajo de un path
 */

public class GetAllFiles {
	
	private File[] files;
	
	public GetAllFiles(String path) {
		files = new File(path).listFiles();		
	}
	
	public Collection<File> getFiles(String path){
		files = new File(path).listFiles();
		return getFiles();
	}
	
	public Collection<File> getFiles() {
		Collection<File> arrayListRet = new ArrayList<File>();
		for(int index=0; index < files.length; index++) {
			if(files[index].isDirectory()) {
				new GetAllFiles(files[index].getPath());
								
			} else{
				arrayListRet.add(files[index]); 
				System.out.println(files[index]);
			}
		}
		return arrayListRet;
	}
	
	

}
