package com.ineuro.simback.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contact")
public class Contact {

	@DatabaseField(generatedId = true)
	private int idContact;
	
	@DatabaseField(canBeNull = false)
	private String fullnameContact;
	
	@DatabaseField(canBeNull = false)
	private String telContact;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Customer ownerContact;

	public int getIdContact() {
		return idContact;
	}

	public void setIdContact(int idContact) {
		this.idContact = idContact;
	}

	public String getFullnameContact() {
		return fullnameContact;
	}

	public void setFullnameContact(String fullnameContact) {
		this.fullnameContact = fullnameContact;
	}

	public String getTelContact() {
		return telContact;
	}

	public void setTelContact(String telContact) {
		this.telContact = telContact;
	}

	public Customer getOwnerContact() {
		return ownerContact;
	}

	public void setOwnerContact(Customer ownerContact) {
		this.ownerContact = ownerContact;
	}
}
