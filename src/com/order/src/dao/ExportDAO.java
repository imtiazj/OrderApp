package com.order.src.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.order.src.ConnectionManager;
import com.order.src.objects.Header;
import com.order.src.objects.SoldTo;

public class ExportDAO {

	// atlantia database
	private String database;

	// bev database
	private String database2;

	public ExportDAO(String database) {
		this.database = database;
	}

	public ExportDAO(String database, String database2) {
		// atlantia database
		this.database = database;
		// bev database
		this.database2 = database2;
	}

	public Connection getConn() throws SQLException {
		return new ConnectionManager().getConnection(database);
	}

	public Connection getConn2() throws SQLException {
		return new ConnectionManager().getConnection(database2);
	}

	// gets header infomation from the new bve database
	public Vector<Header> getHeaderInformation() throws SQLException {
		String sql = "SELECT h.order_no number, h.cust_no custno, h.cust_po_no custpono "
				+ "FROM bve_order h "
				+ "WHERE h.order_no in (select distinct order_no from bve_order_dtl WHERE bvcmtdqty > 0) "
						+ "AND h.ord_status = 'C' "
						//+ "AND (h.ord_status = 'C' OR h.cust_no = 'WALMART.CA') "						//this line gets any walmart data... remove this once the data is okay
				+ "ORDER BY h.order_no";
		Statement stmt = getConn2().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		Vector<Header> headers = new Vector<Header>();

		while (rs.next()) {
			Header header = new Header();
			header.setNo(rs.getString("number"));			//number
			header.setComment(getComments(header.getNo()));
			header.setContact("");
			header.setInvoice("");
			header.setNumber(rs.getString("custno"));	//ref_no
			header.setPoNo(rs.getString("custpono"));
			header.setRefNo("");
			header.setService("");
			header.setShipVia("");
			header.setShipOn("");

			headers.add(header);
		}

		return headers;
	}
	
	private String getComments(String number) throws SQLException {
		String comment = "";
		
		String sql = "SELECT n_data comment FROM bve_notes where n_key = '" + number.trim() + "' order by bvrvadddate, bvrvaddtime";
			
		Statement stmt = getConn2().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		while (rs.next()) {
			comment = rs.getString("comment");
		}
		
		return comment;
	}

	// gets detail information from the new bev database
	public Vector<com.order.src.objects.Line> getDetailLineInformation(String number) throws SQLException {
		String sql = "SELECT order_no number, ord_sequence recno, ord_part_no item, ord_description description, bvcmtdqty qty, comment comment, ord_part_whse warehouse "
				+ "FROM bve_order_dtl "
				+ "WHERE ord_part_no <> '' AND bvcmtdqty > 0 "
				+ "AND order_no like '%" + number.trim() + "%'";

		Statement stmt = getConn2().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		Vector<com.order.src.objects.Line> lines = new Vector<com.order.src.objects.Line>();
		while (rs.next()) {
			com.order.src.objects.Line line = new com.order.src.objects.Line();
			line.setComment(rs.getString("comment"));
			line.setDescription(rs.getString("description"));
			line.setItem(rs.getString("item"));
			line.setLot("");
			line.setNo(rs.getString("recno"));
			line.setQty(rs.getInt("qty"));
			line.setSerial("");
			line.setWarehouse(rs.getString("warehouse").trim());

			lines.add(line);
		}

		return lines;
	}
	
	// gets sold to information from the atlantia database
	public com.order.src.objects.SoldTo getSoldToInformation(String number) throws SQLException {
		String sql = "SELECT a.addr_type addrtype, a.name name, a.bvaddr1 addr1, a.bvaddr2 addr2, a.bvcity city, a.ship_desc shipdesc, "
			+ "a.bvprovstate prov, a.bvcountrycode country, a.bvpostalcode postalcode, a.bvcocontact1name cname, "
			+ "a.bvaddrtelno1 tel1, a.bvaddrtelno2 tel2, a.bvaddremail email "
			+ "FROM order_address a "
			+ "WHERE a.cev_no = '" + number.trim() + "' "
			+ "AND a.addr_type = 'B'";

		Statement stmt = getConn().createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		SoldTo soldTo = new SoldTo();
		while (rs.next()){
			soldTo.setAddress1(rs.getString("addr1"));
			soldTo.setAddress2(rs.getString("addr2"));
			soldTo.setCity(rs.getString("city"));
			soldTo.setCode(rs.getString("addrtype"));
			soldTo.setContact(rs.getString("cname"));
			soldTo.setCountry(rs.getString("country"));
			soldTo.setEmail(rs.getString("email"));
			soldTo.setFax(rs.getString("tel2"));
			soldTo.setName(rs.getString("name"));
			soldTo.setPhone(rs.getString("tel1"));
			soldTo.setPostal(rs.getString("postalcode"));
			soldTo.setProvince(rs.getString("prov"));
		}
		


		return soldTo;
	}

	// gets shipto to information from the atlantia database
	/**
	 * 
	 * @param number (h.order_no number, number in xml) 
	 * @param custno (h.cust_no custno, ref_no in xml)
	 * @return
	 * @throws SQLException
	 */
	public com.order.src.objects.ShipTo getShipToInformation(String number, String custno) throws SQLException {
		String sql = "";
		
		if ("WALMART.CA".equalsIgnoreCase(custno.trim())){
			sql = "SELECT comment comment " +
					"FROM bve_order_dtl " +
					"WHERE order_no like '%" + number.trim() + "%' " +
					"AND ord_sequence = 1";
		}else{
			sql = "SELECT a.addr_type addrtype, a.name name, a.bvaddr1 addr1, a.bvaddr2 addr2, a.bvcity city, a.ship_desc shipdesc, "
				+ "a.bvprovstate prov, a.bvcountrycode country, a.bvpostalcode postalcode, a.bvcocontact1name cname, "
				+ "a.bvaddrtelno1 tel1, a.bvaddrtelno2 tel2, a.bvaddremail email "
				+ "FROM order_address a "
				+ "WHERE a.cev_no = '" + number.trim() + "' "
				+ "AND a.addr_type = 'S'";
		}

		Statement stmt = null;
		if ("WALMART.CA".equalsIgnoreCase(custno.trim())){
			stmt = getConn2().createStatement();
		}else{
			stmt = getConn().createStatement();
		}
		ResultSet rs = stmt.executeQuery(sql);

		com.order.src.objects.ShipTo shipTo = new com.order.src.objects.ShipTo();
		String shipToString = "";
		if ("WALMART.CA".equalsIgnoreCase(custno.trim())){
			while (rs.next()){
				shipToString = rs.getString("comment");
			}
		}else{
			while (rs.next()){
				shipTo.setAddress1(rs.getString("addr1"));
				shipTo.setAddress2(rs.getString("addr2"));
				shipTo.setCity(rs.getString("city"));
				shipTo.setCode(rs.getString("addrtype"));
				shipTo.setContact(rs.getString("cname"));
				shipTo.setCountry(rs.getString("country"));
				shipTo.setEmail(rs.getString("email"));
				shipTo.setFax(rs.getString("tel2"));
				shipTo.setName(rs.getString("name"));
				shipTo.setPhone(rs.getString("tel1"));
				shipTo.setPostal(rs.getString("postalcode"));
				shipTo.setProvince(rs.getString("prov"));
			}
		}
		
		if ("WALMART.CA".equalsIgnoreCase(custno.trim())){
			String[] shipToArray = shipToString.split(","); 
			shipTo.setName(" ");
			shipTo.setAddress1(" ");
			shipTo.setAddress2(" ");
			shipTo.setCity(" ");
			shipTo.setProvince(" ");
			shipTo.setPostal(" ");
			shipTo.setCountry(" ");
			
			//   0          1           2        3       4            5           6
			//<Contact>,<Address1>,<Address2>,<City>,<Province>,<Postal Code>,<Country>
			if (shipToArray.length > 0){
				shipTo.setName(shipToArray[0]);	
			}			
			if (shipToArray.length > 1){
				shipTo.setAddress1(shipToArray[1]);
			}
			if (shipToArray.length > 2){
				shipTo.setAddress2(shipToArray[2]);
			}
			if (shipToArray.length > 3){
				shipTo.setCity(shipToArray[3]);
			}
			if (shipToArray.length > 4){
				shipTo.setProvince(shipToArray[4]);
			}
			if (shipToArray.length > 5){
				shipTo.setPostal(shipToArray[5]);
			}
			if (shipToArray.length > 6){
				if ("CA".equals(shipToArray[6])){
					shipTo.setCountry("CDN");
				}else{
					shipTo.setCountry(shipToArray[6]);
				}
			}
			shipTo.setCode("S");		//address type 'S' for ship
			shipTo.setContact(" ");		//blank, not provided
			shipTo.setEmail(" ");		//blank, not provided
			shipTo.setFax(" ");			//blank, not provided
			shipTo.setPhone(" ");		//blank, not provided
			
			
		}

		return shipTo;
	}
	
	/*
		--WALMART ADDRESS QUERY
		SELECT comment 
		FROM bve_order_dtl
		WHERE order_no like '%00043183-0%'
		AND ord_sequence = 1;
		//<Contact>,<Address1>,<Address2>,<City>,<Province>,<Postal Code>,<Country>
		//Peter Sabiri,1774 Plainridge Cres,,ORL?ANS,ON,K4A 0L9,CA
		//Kurt Nodwell,box 430,4656 district rd 169,Port Carling,ON,P0B 1J0,CA
		
		--WALMART DETAILS QUERY
		SELECT order_no number, ord_sequence recno, ord_part_no item, ord_description description, 
		bvcmtdqty qty, comment comment, ord_part_whse warehouse 
		FROM bve_order_dtl 
		WHERE 1=1 
		AND ord_part_no <> '' 
		AND bvcmtdqty > 0
		AND recno <> 1
		AND order_no like '%00042239-0%';
	*/
}
