package com.ineuro.simback.process;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.jar.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;

public class ImportXml extends DefaultHandler {

	private SAXParser saxParser = null;
	
	/**
	 * Attribut permettant de déterminer la nature de l'élément courant en cours de traitement 
	 * 		Valeurs possibles :
	 * 			0		->		INDERTERMINE
	 * 			1 		->		CONTACT
	 * 			2		->		FULLNAME
	 * 			3		->		PHONE
	 */
	private enum ElementType {
		INDERTERMINE,
		CONTACT,
		FULLNAME,
		PHONE
	}
	private ElementType curEltType = ElementType.INDERTERMINE;
	
	private Customer curClient = null;
	private Contact curContact = null;
	private String content = null;
	
	public ImportXml() {
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		try {
			saxParser = saxFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName,
			String qName, Attributes attribute) throws SAXException  {
		switch (qName) {
		case "contact":
			curEltType = ElementType.CONTACT; 
			curContact = new Contact();
			curContact.setOwnerContact(curClient);
			System.out.println("ImportXml.startElement() : case CONTACT");
			break;
			
		case "fullname":
			curEltType = ElementType.FULLNAME;
			System.out.println("ImportXml.startElement() : case FULLNAME");
			break;

		case "phone":
			curEltType = ElementType.PHONE;
			System.out.println("ImportXml.endElement() : case PHONE");
			break;
		default:
			break;
		}
	}
	
	public void endElement(String uri, String localName, 
    String qName) throws SAXException {
		switch (qName) {
		case "contact":
			// TODO Call here the method to save the current contact
			System.out.println("ImportXml.endElement() : case CONTACT");
			if(null!=curContact) {
				try {
					SimBackDao.createContact(curContact);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				curEltType = ElementType.INDERTERMINE;
			}
			break;
			
		case "fullname":
			System.out.println("ImportXml.endElement() : case FULLNAME");
			if(null==curContact) {
				curContact = new Contact();
				curContact.setOwnerContact(curClient);
			}
			curContact.setFullnameContact(content);
			curEltType = ElementType.CONTACT; 
			break;

		case "phone":
			System.out.println("ImportXml.endElement() : case PHONE");
			if(null==curContact) {
				curContact = new Contact();
				curContact.setOwnerContact(curClient);
			}
			curContact.setTelContact(content);
			curEltType = ElementType.CONTACT; 
			break;
			
		default:
			break;
		}
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
		System.out.println("ImportXml.characters()");
	}
	
	public void parseFile(InputStream is) throws SAXException, IOException {
		saxParser.parse(is, this);
	}

	public Customer getCurClient() {
		return curClient;
	}

	public void setCurClient(Customer curClient) {
		this.curClient = curClient;
	}
}