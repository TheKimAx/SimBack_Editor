package com.ineuro.simback.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "customer")
public class Customer {

	@DatabaseField(generatedId = true)
	private int idCust;
	
	@DatabaseField(canBeNull = false)
	private String nomCust;
	
	@DatabaseField(canBeNull = true)
	private String prenomCust;
	
	@DatabaseField(canBeNull = false)
	private String telCust;

	private static SimpleDateFormat df;
	
	static {
		df = new SimpleDateFormat("dd MMMM");
	}
	
	/* Date d'inscription du client */
	@DatabaseField(canBeNull = false)
	private Date inscrCust;
	
	/* Date du dernier réabonnement */
	@DatabaseField(canBeNull = false)
	private Date lastSubscription;
	
	
	/* Statut d'expiration du compte */
	@DatabaseField(canBeNull = false)
	private boolean expiredCust;
	
	/* Drapeau définissant dans le contexte local 
	 * 		(celui de la machine courant) si ledit compte est celui courant */
	@DatabaseField(canBeNull = false)
	private boolean isDefault = false;

	public int getIdCust() {
		return idCust;
	}

	public void setIdCust(int idCust) {
		this.idCust = idCust;
	}

	public String getNomCust() {
		return nomCust;
	}

	public void setNomCust(String nomCust) {
		this.nomCust = nomCust;
	}

	public String getPrenomCust() {
		return prenomCust;
	}

	public void setPrenomCust(String prenomCust) {
		this.prenomCust = prenomCust;
	}

	public String getTelCust() {
		return telCust;
	}

	public void setTelCust(String telCust) {
		this.telCust = telCust;
	}

	public Date getInscrCust() {
		return inscrCust;
	}

	public void setInscrCust(Date inscrCust) {
		this.inscrCust = inscrCust;
	}

	public Date getLastSubscription() {
		return lastSubscription;
	}

	public void setLastSubscription(Date lastSubscription) {
		this.lastSubscription = lastSubscription;
	}

	public boolean isExpiredCust() {
		return expiredCust;
	}

	public void setExpiredCust(boolean expiredCust) {
		this.expiredCust = expiredCust;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
