package com.neixar.sct.buscaLigas.Process;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Test {
	
	static String protocolo = "(https?|ftp|file)://";
	static String patronServidor = protocolo + "[-a-zA-Z0-9\\.]+";
	static String patronURL = patronServidor + "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	//Método para comprobar el funcionamiento de los regex
	public static void main(String[] args) {
		
		//String linea1 = "param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(\"http://gob/sct/documentos\", \"cUsr\"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName(\"http://www.w3.org/2001/XMLSchema\", \"string\"), java.lang.String.class, false, false);";
		//String linea2 = "java.vendor.url = http://java.sun.com/";
		String linea1 = "private static final String _OCSP = \"http://www.sat.gob.mx/ocsp\";"; 
		
		Pattern patternServer = Pattern.compile(patronServidor);
		Matcher matchServer = patternServer.matcher(linea1);
		
		Pattern patternURL = Pattern.compile(patronURL);
		Matcher matchURL = patternURL.matcher(linea1);
		
		if(matchServer.find()) {
			
			System.out.println(matchServer.group());	//protocolo + server
			System.out.println(matchServer.group().replaceAll(protocolo, "")); //Sin protocolo
			
			if(matchURL.find())
				System.out.println("URL: " +  matchURL.group());
			else
				System.out.println("Sin URL");
			
		}else
			System.out.println("No hay match");
		
	}

}
