package com.neixar.sct.buscaLigas.Files;

import java.io.File;
import java.util.Collection;

public class ListFiles {

	public void getFiles(Collection<File> archivos, File file) {

		if (file.isDirectory()) {
			File[] arrfiles = file.listFiles();
			for (int index = 0; index < arrfiles.length; index++) {
				getFiles(archivos, arrfiles[index]);
			}

		}else
			archivos.add(file);
	}
}
