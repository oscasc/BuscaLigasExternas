package com.neixar.sct.buscaLigas.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashMap;
import com.neixar.sct.buscaLigas.Record.Registro;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class SearchText {

	enum tipoPatron {
		servidor, local, ninguno
	};

	String regexSrv = "(https?|ftp|file)://([-a-zA-Z0-9+&@#%=~_|\\\\.]+)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)";
	String regexLocal = "([cdefgCDEFG]:(\\\\+|//*)([-a-zA-Z0-9+&@#%=~_|.\\s]+))([-a-zA-Z0-9+&@#/%?=~_|!:,\\\\.;\\s]*)";

	/*
	 * Analiza el contenido de la línea en búsqueda del patrón. Si lo encuentra
	 * busca si existe el servidor (HashMap<key>) agrega items a la llave
	 * correspondiente, si no existe la llave, crea una entrada adicional.
	 */
	private void pushLine(HashMap<String, Registro> hash, String fileName, String linea) {

		// Busca patrón de Servidor
		Pattern pattern = Pattern.compile(regexSrv);
		Matcher matcher = pattern.matcher(linea);
		tipoPatron tipo = tipoPatron.ninguno;

		// Estructura Hash
		Registro registro;
		ArrayList<String> urls;
		ArrayList<String> archivos;
		ArrayList<String> ejemplos;

		String url = "";
		String servername = "";

		if (matcher.find()) {
			tipo = tipoPatron.servidor;
			url = matcher.group(0);
			servername = matcher.group(2);
		} else {
			pattern = Pattern.compile(regexLocal);
			matcher = pattern.matcher(linea);
			if (matcher.find()) {
				tipo = tipoPatron.local;
				url = matcher.group(0);
				servername = matcher.group(1);
			}
		}

		// Se encontró un servidor en la línea
		if (tipo != tipoPatron.ninguno) {

			registro = hash.get(servername);

			// Buscamos si existe el servidor.
			if (registro == null) {
				// No encontramos el Registro, creamos uno
				registro = new Registro();
				urls = new ArrayList<String>();
				archivos = new ArrayList<String>();
				ejemplos = new ArrayList<String>();
			} else {
				urls = registro.getUrl();
				archivos = registro.getArchivos();
				ejemplos = registro.getEjemplos();
			}

			// Agregamos información de URLS, Archivos, ejemplos
			try {

				urls.add(url);
				registro.setUrls(urls);

				archivos.add(fileName);
				ejemplos.add(linea);
			} catch (NullPointerException ex) {
				System.out.println("Línea: " + linea);
				ex.printStackTrace();
			}

			registro.setArchivos(archivos);
			registro.setEjemplos(ejemplos);

			hash.put(servername, registro);

		} // No se encontró url en la línea
	}

	/*
	 * Ingresa a un archivo, lee línea a línea, si encuentra el path, lo registra
	 */
	public void processFile(File file, HashMap<String, Registro> hash) {

		//Se usará para evaluar los tipos de extensiones de archivos, válidos a considerar
		String[] validExts = {
				"jsp","js", "java", "html", "htm", "config","xml", "inf","jrxml"
		};
		//Se pondrá en true en caso que el archivo a evaluar coincida con el tipo de extensión en validExts
		boolean processIt = false;
		
		BufferedReader breader = null;
		String fileName;

		if (file.isDirectory())
			return; // Retorna el hashPrincipa, sin cambios.

		fileName = file.getName();

		// Sólo archivos java y js.
		String[] fileExt = fileName.split("\\.");
		
		if(fileExt.length <=0)
			return;
		
		String extension = fileExt[fileExt.length-1]; //Obtenemos extensiones
		for(String validExtension: validExts) {
			if(extension.toLowerCase().equals(validExtension)) {
				processIt = true;
				break;
			}
		}
		
		
		//El archivo no es de una extensión válida.
		//No lo procesaremos
		if (!processIt) 
			return;

		try {
			FileReader freader = new FileReader(file);
			breader = new BufferedReader(freader);

			// Lectura, línea a línea del archivo.
			String line;
			while ((line = breader.readLine()) != null) {
				pushLine(hash, file.getPath(), line);
			}

		} catch (FileNotFoundException ex) {
			// FileReader
			System.out.println("Error:: FileNotFound: " + file.getPath());
			ex.printStackTrace();

		} catch (IOException ex) {
			// lectura, línea a línea
			ex.printStackTrace();
		} finally {
			try {
				breader.close();
			} catch (IOException ex) {
			}
		}

	}
}
