package com.ineuro.simback.dao;

import java.sql.SQLException;
import java.util.List;

import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

public class SimBackDao {

	private static Dao<Customer, Integer> sbCustomerDao;
	private static Dao<Contact, Integer> sbContactDao;
	
	static {
		try {
			sbCustomerDao = DaoManager.createDao(DaoUtils.getConnection(), Customer.class);
			sbContactDao = DaoManager.createDao(DaoUtils.getConnection(), Contact.class);
			
			TableUtils.createTableIfNotExists(DaoUtils.getConnection(), Customer.class);
			TableUtils.createTableIfNotExists(DaoUtils.getConnection(), Contact.class);
			
		} catch(Exception ex) {
			System.out.println("SimBackDao.enclosing_method() : " + ex.getMessage());
		}
	}
	
	public static void createCustomer(Customer aCust) throws SQLException {
		sbCustomerDao.create(aCust);
	}
	
	public static void deleteCustomer(Customer aCust) throws SQLException {
		sbCustomerDao.delete(aCust);
	}
	
	public static void modifyCustomer(Customer aCust) throws SQLException {
		sbCustomerDao.update(aCust);
	}
	
	public static List<Customer> getAllCustomers() throws SQLException {
		return sbCustomerDao.queryForAll();
	}
	
	public static List<Customer> getActiveCustomers() throws SQLException {
		return sbCustomerDao.queryForEq("expiredCust", false);
	}
	
	public static Customer getCustomerWithId(int id) throws SQLException {
		Customer aCust = null;
		aCust = sbCustomerDao.queryForId(id);
		return aCust;
	}
	
	public static Customer getCurrentCustomer() throws SQLException {
		Customer aCust = null;
		List<Customer> lsCusts = sbCustomerDao.queryForEq("isDefault", true);
		if(lsCusts.size() != 0) {
			aCust = lsCusts.get(0);
		} else {
			aCust = null;
//			aCust = new Customer();
//			aCust.setNomCust("DEFAULT");
//			aCust.setPrenomCust("Customer");
//			aCust.setTelCust("+237 6 66 66 66 66");
//			Date aujourdhui = new Date();
//			aCust.setDefault(true);
//			aCust.setExpiredCust(false);
//			aCust.setInscrCust(aujourdhui);
//			aCust.setLastSubscription(aujourdhui);
		}
		return aCust;
	}
	
	public static void changeCurrentCustomer(Customer oldClient, Customer newClient) throws SQLException {
		if(null != oldClient) {
			oldClient.setDefault(false);
			modifyCustomer(oldClient);
		}
		newClient.setDefault(true);
		modifyCustomer(newClient);
	}

	public static void createContact(Contact aCtc) throws SQLException {
		sbContactDao.create(aCtc);
	}
	
	public static void deleteContact(Contact aCtc) throws SQLException {
		sbContactDao.delete(aCtc);
	}
	
	public static void modifyContact(Contact aCtc) throws SQLException {
		sbContactDao.update(aCtc);
	}
	
	public static Contact getContactWithId(int id) throws SQLException {
		Contact aCtc = null;
		aCtc =  sbContactDao.queryForId(id);
		return aCtc;
	}
	
	public static List<Contact> getContactsForClient(Customer customer) throws SQLException {
		List<Contact> lsContacts = null;
		lsContacts = sbContactDao.queryForEq("ownerContact_id", customer.getIdCust());
		return lsContacts;		
	}
}
