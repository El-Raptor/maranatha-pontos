package br.com.sankhya.controller;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Este programa é responsável por interpretar e converter um documento XML para
 * o tipo <code>String</code> ou <code>Document</code>.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-22
 * @version 0.1
 *
 */
public class XMLParser {

	/**
	 * Este método transforma um documento XML em um formato de <code>String</code>.
	 * 
	 * @param doc Este parâmetro é o documento XML no qual está sendo construído.
	 * @return String o documento formatado em <code>String</code>.
	 */
	public static String toString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}

	/**
	 * Este método é responsável por interpretar e converter um documento XML em
	 * tipo de <code>String</code> para o tipo <code>Document</code>.
	 * 
	 * @param xml é o XML no tipo <code>Sting</code> a ser interpretado e convertido
	 *            para <code>Document</code>.
	 * @return Document o produto da interpretação e conversão do documento XML.
	 * @throws Exception em falha ao interpretar e converter a <code>String</code>.
	 */
	public static Document parseXml(String xml) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource src = new InputSource();
		src.setCharacterStream(new StringReader(xml));

		Document doc = builder.parse(src);

		return doc;
	}
}
