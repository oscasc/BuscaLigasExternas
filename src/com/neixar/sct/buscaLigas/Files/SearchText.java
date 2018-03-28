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
import java.util.EmptyStackException;
import java.util.Stack;

public class SearchText {

	enum tipoPatron {
		servidor, local, ninguno
	};

	String regexSrv = "(https?|ftp|file)://([-a-zA-Z0-9+&@#%=~_|\\\\.]+)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)";
	String regexLocal = "([cdefgCDEFG]:(\\\\+|//*)([-a-zA-Z0-9+&@#%=~_|.\\s]+))([-a-zA-Z0-9+&@#/%?=~_|!:,\\\\.;\\s]*)";

	// Para los comentarios que comiencen con: //, exceptuo url
	String regexOneLineComment = "((?<!(https?|ftp|file):)//.+)|(<!--.+-->)|(<%--.+--%>)|(/\\*.*\\*/)|(^[\\t\\s]*\\#.*)";

	// Para identificar los comentarios
	String comentariosBloqueOpen = "(?<!\\/)/\\*|<!--|<%--";
	String comentariosBloqueClose = "\\*/$|-->|--%>";

	/*
	 * Analiza el contenido de la l�nea en b�squeda del patr�n. Si lo encuentra
	 * busca si existe el servidor (HashMap<key>) agrega items a la llave
	 * correspondiente, si no existe la llave, crea una entrada adicional.
	 * 
	 * V2. Antes de ingresar un registro, validar que la l�nea de c�digo no posea
	 * comentarios
	 */
	private void pushLine(HashMap<String, Registro> hash, String fileName, String linea) {

		// Busca patr�n de Servidor
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

		// Se encontr� un servidor en la l�nea
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

			// Agregamos informaci�n de URLS, Archivos, ejemplos
			try {

				// Validamos que no se repita el url para esta entrada
				if (urls.indexOf(url) < 0) {
					urls.add(url);
					registro.setUrls(urls);
				}

				// Validamos que no se repita el archivo para esta entrada
				if (archivos.indexOf(fileName) < 0) {
					archivos.add(fileName);
					registro.setArchivos(archivos);
				}

				// Validamos que no se repita el c�digo para esta entrada
				if (ejemplos.indexOf(linea) < 0) {
					ejemplos.add(linea);
					registro.setEjemplos(ejemplos);
				}

			} catch (NullPointerException ex) {
				System.out.println("L�nea: " + linea);
				ex.printStackTrace();
			}

			hash.put(servername, registro);

		} // No se encontr� url en la l�nea
	}

	/*
	 * Ingresa a un archivo, lee l�nea a l�nea, si encuentra el path, lo registra
	 */
	public void processFile(File file, HashMap<String, Registro> hash) {

		// Se usar� para evaluar los tipos de extensiones de archivos, v�lidos a
		// considerar
		String[] validExts = { "jsp", "js", "java", "html", "htm", "properties", "xml", "inf", "jrxml" };
		// Se pondr� en true en caso que el archivo a evaluar coincida con el tipo de
		// extensi�n en validExts
		boolean processIt = false;

		BufferedReader breader = null;
		String fileName;

		if (file.isDirectory())
			return; // Retorna el hashPrincipa, sin cambios.

		fileName = file.getName();

		// S�lo archivos con extensiones conocidas
		String[] fileExt = fileName.split("\\.");

		if (fileExt.length <= 0)
			return;

		String extension = fileExt[fileExt.length - 1]; // Obtenemos extensiones
		for (String validExtension : validExts) {
			if (extension.toLowerCase().equals(validExtension)) {
				processIt = true;
				break;
			}
		}

		// El archivo no es de una extensi�n v�lida.
		// No lo procesaremos
		if (!processIt)
			return;

		try {

			// Esta pila se utiliza para hacer un a parse del codigo
			// y detectar bloques de comentarios.
			Stack<String> stack = new Stack<String>();

			FileReader freader = new FileReader(file);
			breader = new BufferedReader(freader);

			// Lectura, l�nea a l�nea del archivo.
			String line;
			int lineNum = 0;
			while ((line = breader.readLine()) != null) {
				lineNum++; // Para conocer el n�mero de l�nea: Usado de Depuraci�n
				
				//Si es l�nea vac�a, ignorar y continuar
				if(line.trim().equals(""))
					continue;
				
				line = line.trim();
				
				// Elimina l�neas con comentario de l�nea al inicio.
				Pattern patLineComment = Pattern.compile(regexOneLineComment);
				Matcher matchComment = patLineComment.matcher(line);

				// Eliminamos de la l�nea, los comentarios que pudieran existir
				if (matchComment.find())
					line = matchComment.replaceAll("");

				// V2: Analizar uso de bloques de comentarios

				Pattern pattBOpen = Pattern.compile(comentariosBloqueOpen);
				Matcher matchBOpen = pattBOpen.matcher(line);
				if (matchBOpen.find()) {
					stack.push(matchBOpen.group().trim());
					//System.out.println("PUSH: " + line); //debug
				}

				Pattern pattBClose = Pattern.compile(comentariosBloqueClose);
				Matcher matchBClose = pattBClose.matcher(line);
				if (matchBClose.find()) {
					try {
						//System.out.println("POP:" + line); //debug
						stack.pop();
					} catch (EmptyStackException e) {
						System.out.println("Hubo un error en la l�nea:" + lineNum);
						System.out.println(line);
						System.out.println("Con el comentario: " + comentariosBloqueClose);
						System.out.println("archivo: " + file.getAbsolutePath());
						e.printStackTrace();

					}
				}

				if (stack.isEmpty()) // Procesa, s�lo si la pila est� vac�a
				{
					pushLine(hash, file.getPath(), line);
				}
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
