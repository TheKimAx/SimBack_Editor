package com.ineuro.simback.process;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;

public class ExportXml {

	private static Customer curClient = null;
//	private static List<Contact> lsContacts = null;
	
	private static DocumentBuilderFactory docBuilderFactory = null;
	private static DocumentBuilder docBuilder = null;
	private static Document curDoc = null;
	
	private static void buildXMLDoc(List<Contact> lsContacts) throws ParserConfigurationException {
		docBuilder = docBuilderFactory.newDocumentBuilder();
		curDoc = docBuilder.newDocument();
		
		/* Création de l'élément racine */
		Element eltRoot = curDoc.createElement("simback_directory");
		/* Création du noeud descriptif du client */
		Element eltCust = getClientNode();
		eltRoot.appendChild(eltCust);
		
		/* Création du noeud contenant les contacts */
		Element eltContacts = curDoc.createElement("contacts");		
		for(Contact aContact : lsContacts)
			eltContacts.appendChild( getContactNode(aContact) );
		eltRoot.appendChild(eltContacts);
		
		/* Ajout de l'élément racine au document */
		curDoc.appendChild(eltRoot);
	}

	/**
	 * Création du noeud descriptif du client
	 * @return Element corresponding to the descriptive node of current customer
	 */
	private static Element getClientNode() {
		Element eltCust = curDoc.createElement("client");
		
		Element child = curDoc.createElement("nom");
		child.appendChild(curDoc.createTextNode(curClient.getNomCust()));
		eltCust.appendChild(child);
		
		child = curDoc.createElement("prenom");
		child.appendChild(curDoc.createTextNode(curClient.getPrenomCust()));
		eltCust.appendChild(child);
		
		child = curDoc.createElement("email");
		child.appendChild(curDoc.createTextNode(curClient.getEmailCust()));
		eltCust.appendChild(child);
		
		child = curDoc.createElement("telephone");
		child.appendChild(curDoc.createTextNode(curClient.getTelCust()));
		eltCust.appendChild(child);
		
		child = curDoc.createElement("cni");
		child.appendChild(curDoc.createTextNode(curClient.getNoCni()));
		eltCust.appendChild(child);
		
		return eltCust;
	}
	
	private static Element getContactNode(Contact aCtc) {
		Element eltContact = curDoc.createElement("contact");
		
		Element child = curDoc.createElement("fullname");
		child.appendChild(curDoc.createTextNode(aCtc.getFullnameContact()));
		eltContact.appendChild(child);
		
		child = curDoc.createElement("phone");
		child.appendChild(curDoc.createTextNode(aCtc.getTelContact()));
		eltContact.appendChild(child);
		
		return eltContact;
	}
	
	public static boolean saveContactsToXmlFile(String filePath, List<Contact> lsContacts, Customer curClient) 
			throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		boolean res = false;
		ExportXml.curClient = curClient;
		
		/* Appel de la méthode de construction effective du contenu du fichier */
		buildXMLDoc(lsContacts);
		
		/* Formatage du contenu du fichier */
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(curDoc);
		StreamResult fileOutput =  new StreamResult(new File(filePath));
		transformer.transform(source, fileOutput);
		res = true;
		
		return res;
	}
}
