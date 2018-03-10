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

	String protocolo = "(https?|ftp|file)://";
	String patronServidor = protocolo + "[-a-zA-Z0-9\\.]+";
	String patronURL = patronServidor + "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	/*
	 * Analiza el contenido de la l�nea en b�squeda del patr�n. Si lo encuentra
	 * busca si existe el servidor (HashMap<key>) agrega items a la llave
	 * correspondiente, si no existe la llave, crea una entrada adicional.
	 */
	private void pushLine(HashMap<String, Registro> hash, String fileName, String linea) {

		// Busca patr�n de Servidor
		Pattern patternSrv = Pattern.compile(patronServidor);
		Matcher matcherSrv = patternSrv.matcher(linea);
		Pattern patternURL;
		Matcher matcherURL;
				

		// Estructura Hash
		Registro registro;
		ArrayList<String> urls;
		ArrayList<String> archivos;
		ArrayList<String> ejemplos;

		// Se encontr� un servidor en la l�nea
		if (matcherSrv.find()) {

			//Nombre del Servidor sin el protocolo
			String nombreServidor = matcherSrv.group().replaceAll(protocolo, ""); 
			
			registro = hash.get(nombreServidor);

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

			// Agregamos informaci�n de URLS, Archivos, ejemplos
			patternURL = Pattern.compile(patronURL);
			matcherURL = patternURL.matcher(linea);					
			String url;
			
			if(matcherURL.find()) {
				url = matcherURL.group();
				urls.add(url);
				registro.setUrls(urls);
			}
			
			archivos.add(fileName);
			ejemplos.add(linea);

			
			registro.setArchivos(archivos);
			registro.setEjemplos(ejemplos);

			hash.put(nombreServidor, registro);

		}//No se encontr� url en la l�nea
	}

	/*
	 * Ingresa a un archivo, lee l�nea a l�nea, si encuentra el path, lo registra
	 */
	public void processFile(File file, HashMap<String, Registro> hash) {

		BufferedReader breader = null;
		String fileName;		

		if (file.isDirectory())
			return; // Retorna el hashPrincipa, sin cambios.
		
		fileName = file.getName();
		//extension = fileName.substring(fileName.indexOf("."),fileName.length());
		
		//S�lo archivos java y js.
		if(!fileName.toLowerCase().endsWith(".java") && !fileName.toLowerCase().endsWith(".js") )
			return;

		try {
			FileReader freader = new FileReader(file);
			breader = new BufferedReader(freader);

			// Lectura, l�nea a l�nea del archivo.
			String line;
			while ((line = breader.readLine()) != null) {
				pushLine(hash, file.getPath(), line);
			}

		} catch (FileNotFoundException ex) {
			// FileReader
			System.out.println("Error:: FileNotFound: " + file.getPath());
			ex.printStackTrace();

		} catch (IOException ex) {
			// lectura, l�nea a l�nea
			ex.printStackTrace();
		} finally {
			try {
				breader.close();
			} catch (IOException ex) {
			}
		}

	}
}
