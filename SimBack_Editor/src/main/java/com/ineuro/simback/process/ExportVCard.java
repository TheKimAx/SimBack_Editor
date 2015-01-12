package com.ineuro.simback.process;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.ineuro.simback.model.Contact;

import ezvcard.VCard;
import ezvcard.VCardException;
import ezvcard.io.VCardWriter;
import ezvcard.types.FormattedNameType;
import ezvcard.types.TelephoneType;

public class ExportVCard {

		
	public static boolean saveContactsToVcardFile(String filePath, List<Contact> lsContacts)
			throws IOException, VCardException {
		boolean res = false;
		VCardWriter vcw = new VCardWriter(new FileWriter(filePath));
		
		for(Contact aCtc : lsContacts) {
			VCard aVCard = new VCard();
			
			aVCard.addFormattedName(new FormattedNameType(aCtc.getFullnameContact()));
			aVCard.addTelephoneNumber(new TelephoneType(aCtc.getTelContact()));
			vcw.write(aVCard);
		}
		res = true;
		
		vcw.close();
		return res;
	}
}