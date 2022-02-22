package br.com.sankhya.controller;

/**
 * Este programa é responsável por criar uma URL para o serviço de alteração de
 * nota.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-21
 * @version 0.1
 *
 */
public class LoggedURL extends Connection {

	public final static String SERVICE_NAME = "CACSP.incluirAlterarCabecalhoNota";

	/**
	 * Este método é responsável por criar uma URL para o serviço de alteração de
	 * nota.
	 * @param jsessionid o ID da sessão de login atual.
	 */
	public LoggedURL(String jsessionid) {
		url = "http://" + IP_ADDRESS + ":" + PORT + "/mgecom/service.sbr?serviceName=" + SERVICE_NAME + "&mgeSession="
				+ jsessionid + "&outputType=xml";
	}
}
