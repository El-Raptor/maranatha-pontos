package br.com.sankhya.controller;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Este programa � respons�vel por abrir uma conex�o com um servi�o HTTP.
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
	 * Este m�todo � respons�vel por abrir a conex�o com o endere�o do servi�o.
	 * 
	 * @return <code>HttpURLConnection</code> retorna uma conex�o.
	 * @throws Exception falha em opera��es de E/S ou na forma��o do URL.
	 */
	public HttpURLConnection openConnection() throws Exception {

		URL urlCon = new URL(url);
		return (HttpURLConnection) urlCon.openConnection();
	}

}
