package br.com.sankhya.controller;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.com.sankhya.model.Nota;

/**
 * Este programa constrói o corpo da requisição HTTP em formato XML para os
 * serviços de Login e Alteração da Nota.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-22
 * @version 0.1
 *
 */
public class XMLBuilder {
	/**
	 * Este método é responsável por criar o corpo de requisição para o serviço de
	 * <b>Login</b>.
	 * 
	 * @param serviceName é o nome do serviço no qual terá o seu corpo criado.
	 * @return Document o documento XML criado para a requisição do serviço.
	 */
	public static Document createLoginBody(String serviceName) {

		Document doc = null;

		try {
			DocumentBuilderFactory doFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = doFactory.newDocumentBuilder();

			// Root elements
			doc = builder.newDocument();

			Element serviceRequest = createServiceElement(doc, serviceName);
			Element requestBody = createXmlElement(doc, "requestBody", serviceRequest);

			createCredentials(doc, requestBody);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		/*
		 * String xmlBody = "<serviceRequest serviceName=\"MobileLoginSP.login\">" +
		 * "<requestBody><NOMUSU>SUP</NOMUSU><INTERNO>tecsis</INTERNO></requestBody></serviceRequest>";
		 */

		return doc;
	}

	/**
	 * Este método cria o corpo da requisição do serviço de <b>alteração de
	 * nota</b>.
	 * 
	 * @param nota        Este parâmetro é o modelo de Nota no qual contém as
	 *                    informações a serem alteradas.
	 * @param serviceName é o nome do serviço no qual terá o seu corpo criado.
	 * @return Document o documento XML criado para a requisição do serviço.
	 */
	public static Document createNotaBody(Nota nota, String serviceName) {

		Document doc = null;

		try {
			DocumentBuilderFactory doFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = doFactory.newDocumentBuilder();

			// Root elements
			doc = builder.newDocument();

			Element serviceElement = createServiceElement(doc, serviceName);
			Element requestBody = createXmlElement(doc, "requestBody", serviceElement);
			Element cab = createXmlElement(doc, "nota", requestBody);
			Element cabecalho = createXmlElement(doc, "cabecalho", cab);

			createFieldsElements(doc, cabecalho, nota);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}

		return doc;
	}

	/**
	 * Este método é responsável por criar um elemento Tag no documento XML.
	 * 
	 * @param doc     Este parâmetro é o documento XML no qual está sendo
	 *                construído.
	 * @param tagName Este parâmetro é o nome da tag XML na qual está sendo criada.
	 * @param parent  Este parâmetro representa o elemento pai desse elemento a ser
	 *                criado.
	 * @return Element retorna um elemento, neste caso uma Tag XML, para o documento
	 *         XML.
	 */
	private static Element createXmlElement(Document doc, String tagName, Element parent) {
		Element element = doc.createElement(tagName);

		parent.appendChild(element);

		return element;
	}

	/**
	 * Este método é responsável por criar um elemento Tag no documento XML.
	 * 
	 * @param doc        Este parâmetro é o documento XML no qual está sendo
	 *                   construído.
	 * @param tagName    Este parâmetro é o nome da tag XML na qual está sendo
	 *                   criada.
	 * @param attributes Este parâmetro é o mapa no qual a sua chave servirá como o
	 *                   nome do atributo, e seu valor como o valor do mesmo.
	 * @param parent     Este parâmetro representa o elemento pai desse elemento a
	 *                   ser criado.
	 * @return Element retorna um elemento, neste caso uma Tag XML, para o documento
	 *         XML.
	 */
	private static void createXmlElement(Document doc, String tagName, String content, Element parent) {
		Element element = doc.createElement(tagName);

		element.appendChild(doc.createTextNode(content));

		parent.appendChild(element);
	}

	/**
	 * Este método é responsável por criar um elemento Tag no documento XML.
	 * 
	 * @param doc        Este parâmetro é o documento XML no qual está sendo
	 *                   construído.
	 * @param tagName    Este parâmetro é o nome da tag XML na qual está sendo
	 *                   criada.
	 * @param attributes Este parâmetro é o mapa no qual a sua chave servirá como o
	 *                   nome do atributo, e seu valor como o valor do mesmo.
	 * @return Element retorna um elemento, neste caso uma Tag XML, para o documento
	 *         XML.
	 */
	private static Element createXmlElement(Document doc, String tagName, HashMap<String, String> attributes) {
		Element element = doc.createElement(tagName);

		/* Obtém a chave do mapa e o seu valor e os inserem como atributo da Tag. */
		for (Map.Entry<String, String> entry : attributes.entrySet())
			element.setAttribute(entry.getKey(), entry.getValue());

		doc.appendChild(element);

		return element;
	}

	/**
	 * Este método é responsável por criar um elemento Tag com o nome do serviço no
	 * documento XML.
	 * 
	 * @param doc         Este parâmetro é o documento XML no qual está sendo
	 *                    construído.
	 * @param serviceName Este parâmetro é o nome do serviço que será colocado no
	 *                    valor do atributo <i>serviceName</i>.
	 * @return Element retorna um elemento, neste caso uma Tag XML, para o documento
	 *         XML.
	 */
	private static Element createServiceElement(Document doc, String serviceName) {
		HashMap<String, String> serviceAttr = new HashMap<String, String>();
		serviceAttr.put("serviceName", serviceName);
		Element serviceRequest = createXmlElement(doc, "serviceRequest", serviceAttr);

		return serviceRequest;
	}

	/**
	 * Este método é responsável por criar as Tags XML que representam os campos a
	 * serem modificados.
	 * 
	 * @param doc    Este parâmetro é o documento XML no qual está sendo construído.
	 * @param parent Este parâmetro representa o elemento pai desse elemento a ser
	 *               criado.
	 * @param nota   Este parâmetro é o modelo de Nota no qual contém as informações
	 *               a serem alteradas.
	 */
	private static void createFieldsElements(Document doc, Element parent, Nota nota) {
		createXmlElement(doc, "NUNOTA", String.valueOf(nota.getNunota()), parent);
		createXmlElement(doc, "VLRDESCTOT", String.valueOf(nota.getVlrdesctot()), parent);
		createXmlElement(doc, "PERCDESC", String.valueOf(nota.getPercdesc()), parent);
		createXmlElement(doc, "AD_DESCPREMIO", String.valueOf(nota.getAd_descpremio()), parent);
	}

	/**
	 * Este método é responsável por criar as Tags XML com as credenciais para a
	 * realização do login.
	 * 
	 * @param doc    Este parâmetro é o documento XML no qual está sendo construído.
	 * @param parent Este parâmetro representa o elemento pai desse elemento a ser
	 *               criado.
	 */
	private static void createCredentials(Document doc, Element parent) {
		createXmlElement(doc, "NOMUSU", "SUP", parent);
		createXmlElement(doc, "INTERNO2", "13e883435bd1dedf7df48c8b3d1777ba", parent);
	}
}
