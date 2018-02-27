package com.neixar.sct.buscaLigas.Files;

import java.io.File;
import java.util.Collection;
import java.util.ArrayList;

public class ListFiles {

	public void getFiles(File file){
		
		if(file.isDirectory()) {
			File[] arrfiles = file.listFiles();
			for(int index = 0 ; index < arrfiles.length ; index++) {
				getFiles(arrfiles[index]);
			}
			
		}		
		System.out.println(file.getPath());		
	}
}
