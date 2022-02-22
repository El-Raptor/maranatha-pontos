package br.com.sankhya.controller;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Este programa é responsável por abrir uma conexão com um serviço HTTP.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-15
 * @version 0.1
 *
 */
public class Connection {
	final static String IP_ADDRESS = "";
	final static String PORT = "8280";
	String url;

	/**
	 * Este método é responsável por abrir a conexão com o endereço do serviço.
	 * 
	 * @return <code>HttpURLConnection</code> retorna uma conexão.
	 * @throws Exception falha em operações de E/S ou na formação do URL.
	 */
	public HttpURLConnection openConnection() throws Exception {

		URL urlCon = new URL(url);
		return (HttpURLConnection) urlCon.openConnection();
	}

}
