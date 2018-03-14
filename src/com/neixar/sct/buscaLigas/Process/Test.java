package com.neixar.sct.buscaLigas.Process;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Test {

	/*
	 * tipo de búsquedas:
	 * 
	 * https://sitioweb.com/ {Server name:=> sitioweb.com}
	 * c:/directorio/subdirectorio {Server name:=> c:/directorio}
	 * 
	 * regex:
	 * (https?|ftp|file)://[-a-zA-Z0-9+&@#%=~_|\.]+[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*
	 */

	// Método para comprobar el funcionamiento de los regex
	public static void main(String[] args) {

		String regexServer = "(https?|ftp|file)://([-a-zA-Z0-9+&@#%=~_|\\\\.]+)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)";
		String regexLocal = "([cdefgCDEFG]:(\\\\+|//*)([-a-zA-Z0-9+&@#%=~_|.\\s]+))([-a-zA-Z0-9+&@#/%?=~_|!:,\\\\.;\\s]*)";

		Pattern patron;
		Matcher match;

		String[] lineas = {
				"param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(\"http://gob/sct/documentos\", \"cUsr\"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(\"http://www.w3.org/2001/XMLSchema\", \"string\"), java.lang.String.class, false, false);",
				"java.vendor.url = http://java.sun.com/",
				"private static final String _OCSP = \"http://www.sat.gob.mx/ocsp\";",
				"File dir = new File(\"C:\\Users\\Ricardo Ruben\\Desktop\\PlanVuelo\\Exitosos\");",
				"URLEstaticos=c:/wwwrooting/",
				"String path = \"C://Users//d780//Documents//Jasper_AcuseRecibo_Modificando//TRAAcuseRecibo.jasper\";",
				"File destino = new File(\"C:\\\\Users\\\\Ricardo Ruben\\\\Desktop\\\\PlanVuelo\\\\Registrados\");" };

		for (String linea : lineas) {

			System.out.println("\n\n=======================");
			System.out.println("linea: " + linea);

			patron = Pattern.compile(regexServer);
			match = patron.matcher(linea);
			if (match.find()) {
				// Se encontró un patrón.

				// Determinamos si es Local o Servidor
				// if (matchServidor.find()) {
				// Es tipo servidor
				System.out.println("Servidor: " + match.group(2));
				System.out.println("URL : " + match.group(0));

			} else {
				patron = Pattern.compile(regexLocal);
				match = patron.matcher(linea);
				if(match.find()) {
					System.out.println("Local: " + match.group(1));
					System.out.println("URL : " + match.group());
				} else
					System.out.println("Que siempre ya no");

			}

		}

	}

}
