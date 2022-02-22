package br.com.sankhya.controller;

/**
 * Este programa � respons�vel por criar uma URL para o servi�o de login.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-21
 * @version 0.1
 *
 */
public class LoginURL extends Connection {
	public final static String SERVICE_NAME = "MobileLoginSP.login";

	/*
	 * Este m�todo � respons�vel por criar uma URL para o servi�o de login.
	 */
	public LoginURL() {
		url = "http://" + IP_ADDRESS + ":" + PORT + "/mge/service.sbr?serviceName=" + SERVICE_NAME;
	}
}
