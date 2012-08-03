package com.order.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

	private String URL = "jdbc:odbc:DefaultDB";
	private String BLANK_URL = "jdbc:odbc:";

	public Connection getConnection() throws SQLException {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException ee) {
			ee.printStackTrace();
		}
		Connection c;
		c = DriverManager.getConnection(URL);

		return c;
	}
	
	public Connection getConnection(String database) throws SQLException {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException ee) {
			ee.printStackTrace();
		}
		Connection c;
		c = DriverManager.getConnection(BLANK_URL + database);

		return c;
	}

	public void testConnection(Connection c, String sql) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		int counter = 0;
		
		while(rs.next()){
			++counter;
		}
		
		System.out.println("Size: " + counter);
		
	}
}
