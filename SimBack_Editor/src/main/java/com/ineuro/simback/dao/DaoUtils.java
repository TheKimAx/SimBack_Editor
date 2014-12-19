package com.ineuro.simback.dao;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class DaoUtils {

	private static String dbUrl = "jdbc:sqlite:simback.db3";
	private static JdbcConnectionSource conSrc;
	
	static {
		try {
			conSrc = new JdbcConnectionSource(dbUrl);
		} catch(SQLException e) {
			System.out.println("DaoUtils.enclosing_method() : Erreur lors de l'accès à la BD");
			e.printStackTrace();
		}
	}
	
	public static JdbcConnectionSource getConnection() {
		return conSrc;
	}
}
