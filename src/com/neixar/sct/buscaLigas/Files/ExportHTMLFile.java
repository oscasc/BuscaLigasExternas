package com.neixar.sct.buscaLigas.Files;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import com.neixar.sct.buscaLigas.Record.Registro;




public class ExportHTMLFile {

	private HashMap<String, Registro> hash;
	private File file;

	public ExportHTMLFile(String fileName, HashMap<String, Registro> hash) {
		this.hash = hash;
		file = new File(fileName.trim()+".html");
	}

	public void writeFile() {
		String header;
		String footer;
		String body;
		try {

			if (file.exists())
				file.delete();

			FileWriter fwriter = new FileWriter(file);

			header = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "  <head>\r\n" + "    <meta charset=\"utf-8\">\r\n"
					+ "    <title>Documento test</title>\r\n" + "  </head>\r\n" + "  <body>\r\n" + "    <table>\r\n"
					+ "      <tr>\r\n" + "        <th>Servidor</th>\r\n" + "        <th>URL</th>\r\n"
					+ "        <th>Archivos</th>\r\n" + "        <th>Ejemplos</th>\r\n" + "      </tr>";

			fwriter.write(header);

			footer = "    </table>\r\n" + "  </body>\r\n" + "</html>";

			for (Map.Entry<String, Registro> entry : hash.entrySet()) {
				String servidor = entry.getKey();
				Registro registro = entry.getValue();
				ArrayList<String> archivos = registro.getArchivos();
				ArrayList<String> ejemplos = registro.getEjemplos();
				ArrayList<String> urls = registro.getUrl();

				body = "<tr>\r\n" + "        <td>" + servidor + "</td>\r\n" + "        <td>";
				for (String url : urls) {
					body += url + "<br>";
				}

				body += "</td>\r\n" + "        <td>";

				for (String archivo : archivos) {
					body += archivo + "<br>";
				}

				body += "</td>\r\n" + "        <td>";

				for (String ejemplo : ejemplos) {
					if(ejemplo.trim().startsWith("*") || ejemplo.trim().startsWith("//"))
						continue;
					
					if(ejemplo.length()>500)
						ejemplo = ejemplo.substring(0, 499);
					
					body += "<pre>" + ejemplo + "</pre><br>";
				}

				body += "</td>\r\n" + "      </tr>\r\n";

				fwriter.write(body);
								
			}
			fwriter.write(footer);
			fwriter.close();

		} catch (IOException ex) {
			System.out.println("Error::ExportFile.write:");
			ex.printStackTrace();
		}
	}

}
