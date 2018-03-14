package com.neixar.sct.buscaLigas.Process;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestGrp {
	
	/*
	 * Para archivos locales: 
	 * 	Group(0) nos trae el URL completo
	 * 	Group(1) nos trae el Servidor (C:/directorio/)
	 * 
	 * PAra Servidores
	 * 	Group(0) nos trae el URL completo
	 * 	Group(2) nos trae el Servidor (C:/directorio/)
	 * */

	public static void main(String[] args) {
		String patron = "([cdefgCDEFG]:(\\\\+|//*)([-a-zA-Z0-9+&@#%=~_|.\\s]+))([-a-zA-Z0-9+&@#/%?=~_|!:,\\\\.;\\s]*)";
		String linea = "File destino = new File(\"C:\\Users 2\\Ricardo Ruben\\Desktop\\PlanVuelo\\Registrados\");";
		
		//String patron = "(https?|ftp|file)://([-a-zA-Z0-9+&@#%=~_|\\\\.]+)([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)";
		//String linea = "(new java.net.URL(\"http://ws.sct.gob.mx/wsADAuth/Authentication?WSDL\"));";
		
		Pattern pattern = Pattern.compile(patron);
		Matcher matcher = pattern.matcher(linea);
		
		if(matcher.find()) {
									
			System.out.println("Group:" + matcher.group());
			
			for(int index = 0; index <= matcher.groupCount(); index++) {
				System.out.println("Group(" +  index + ") = " + matcher.group(index));
			}
			
			
			
		}
		
		
		

	}

}
