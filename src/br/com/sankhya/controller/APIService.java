package br.com.sankhya.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import br.com.sankhya.model.Nota;

/**
 * 
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-21
 * @version 0.1
 *
 */
public class APIService {

	/**
	 * Este método é responsável por realizar a requisição do serviço de Login e obter o ID da Sessão.
	 * 
	 * 
	 * @return <code>String</code> o ID da sessão obtido após o login.
	 * @throws DOMException 
	 * @throws Exception em falha no DOM (Document Object Model) ou falha em operações de E/S ou na formação do URL.
	 */
	public static String login() throws Exception  {

		Connection url = new LoginURL();
		HttpURLConnection con = url.openConnection();
		String xml = XMLParser.toString(XMLBuilder.createLoginBody(LoginURL.SERVICE_NAME));

		requestPost(con, xml);

		//return XMLParser.toString(response(con));
		return response(con).getElementsByTagName("jsessionid").item(0).getTextContent();
	}
	
	/**
	 * Este método é responsável por atualizar a nota
	 * 
	 * @param nota
	 * @return
	 * @throws Exception
	 */
	public static String updateNota(Nota nota) throws Exception {

		Connection url = new LoggedURL(login());
		HttpURLConnection con = url.openConnection();
		String xml = XMLParser.toString(XMLBuilder.createNotaBody(nota, LoginURL.SERVICE_NAME));

		requestPost(con, xml);
		
		return response(con).getElementsByTagName("serviceResponse").item(0).getAttributes().item(2).getTextContent();
	}

	private static void requestPost(HttpURLConnection con, String xml) throws Exception {

		con.setRequestMethod("POST");

		// Set the request Content-type Header Parameter
		con.setRequestProperty("Content-type", "text/xml; charset=ISO-8859-1");

		// Set response format type
		con.setRequestProperty("Accept", "application/xml");

		// Ensure the connection will be used to send content
		con.setDoOutput(true);

		// Write XML
		OutputStream outputStream = con.getOutputStream();
		byte[] b = xml.getBytes("UTF-8");
		outputStream.write(b);
		outputStream.flush();
		outputStream.close();
	}

	private static Document response(HttpURLConnection con) throws Exception {
		InputStream inputStream = con.getInputStream();
		byte[] res = new byte[2048];
		int i = 0;
		StringBuilder response = new StringBuilder();
		while ((i = inputStream.read(res)) != -1) {
			response.append(new String(res, 0, i));
		}
		inputStream.close();

		return XMLParser.parseXml(response.toString());
	}

}
