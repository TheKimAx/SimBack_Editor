package com.ineuro.simback.process;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;

import ezvcard.VCard;
import ezvcard.VCardException;
import ezvcard.io.VCardReader;

public class ImportVCard {
	
	public static Customer curClient = null;
	
	public static List<Contact> parseFile(String vFilePath) throws VCardException, IOException, SQLException {
		VCardReader vcr =  new VCardReader(new FileReader(vFilePath));
		VCard aVCard = null;
		List<Contact> lsContacts = new ArrayList<>();
		int  i = 0;
		
		while( (aVCard = vcr.readNext()) != null) {
			System.out.println("ImportVCard.parseFile() Taille FormattedName : " + aVCard.getFormattedNames().size());
			System.out.println("ImportVCard.parseFile() Taille TelephoneNumbers : " + aVCard.getTelephoneNumbers().size());
			Contact aContact = new Contact();
			aContact.setOwnerContact(curClient);
			aContact.setFullnameContact((aVCard.getFormattedNames().size() == 0) ? "" : aVCard.getFormattedName().getValue());
			aContact.setTelContact((aVCard.getTelephoneNumbers().size() == 0) ? "" : aVCard.getTelephoneNumbers().get(0).getValue());
			System.out.println("ImportVCard.parseFile() Contact Trouvé " + ++i +" : "
					+ aContact.getFullnameContact() + " - "
					+ aContact.getTelContact() );
			lsContacts.add(aContact);
			SimBackDao.createContact(aContact);
		}
		
		vcr.close();
		return lsContacts;
	}

	public static Customer getCurClient() {
		return curClient;
	}

	public static void setCurClient(Customer curClient) {
		ImportVCard.curClient = curClient;
	}
}
