package com.order.src;

import java.sql.Connection;
import java.sql.SQLException;

import noNamespace.OrdersDocument;
import noNamespace.OrdersDocument.Orders;
import noNamespace.OrdersDocument.Orders.Order;
import noNamespace.OrdersDocument.Orders.Order.Details;
import noNamespace.OrdersDocument.Orders.Order.Header;
import noNamespace.OrdersDocument.Orders.Order.Details.Line;
import noNamespace.OrdersDocument.Orders.Order.Header.Soldto;

public class TestXMLnConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test();
	}

	private static void test() {
		OrdersDocument ordsDoc = null;

		ordsDoc = OrdersDocument.Factory.newInstance();
		Orders newOrders = ordsDoc.addNewOrders();
		newOrders.setCustomer((short) 2);
		
		//------------
		
		Order newOrder = newOrders.addNewOrder();
		newOrder.setNo((short) 3);

		Header newHeader = newOrder.addNewHeader();
		newHeader.setNumber("5");

		//-------------
		
		Details newDetails = newOrder.addNewDetails();
		Line newLine = newDetails.addNewLine();
		newLine.setNo((short) 33);
		newLine.setItem("Chill");
		newLine.setQty("7");
		Line newLine1 = newDetails.addNewLine();
		newLine1.setNo((short) 66);
		newLine1.setItem("Chill Beans");
		newLine1.setQty("19");

		Soldto newSoldto = newHeader.addNewSoldto();
		newSoldto.setName("Imtiaz");
		newSoldto.setAddress1("1960 7th Ave W");
		newSoldto.setCity("Vancouver");
		newSoldto.setCountry("Canada");
		newSoldto.setProvince("BC");
		newSoldto.setPostal("V7J1T1");

		/*
		System.out.println(ordsDoc.toString());
		System.out.println(ordsDoc.validate());
		*/
		
		/*
		try {
			ordsDoc.save(new File("c:/test.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		String sql = "SELECT h.number number, h.cust_no custno, h.cust_po_no custpono, a.addr_type addrtype, " + 
		"a.name name, a.bvaddr1 addr1, a.bvaddr2 addr2, a.bvcity city, a.ship_desc shipdesc, " + 
		"a.bvprovstate prov, a.bvcountrycode country, a.bvpostalcode postalcode, a.bvcocontact1name contactname, " + 
		"a.bvaddrtelno1 tel1, a.bvaddrtelno2 tel2, a.bvaddremail email " + 
		"FROM sales_order_header h " + 
		"LEFT JOIN order_address a " + 
		"ON h.bvaddr_cev_no = a.cev_no " + 
		"WHERE h.number in (select distinct number from sales_order_detail) " +
		"AND a.addr_type = 'B' " + 
		"ORDER BY h.number, a.addr_type";
		
		ConnectionManager conman = new ConnectionManager();
		try {
			Connection con = conman.getConnection();
			conman.testConnection(con, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
