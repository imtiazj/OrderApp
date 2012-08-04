package com.order.src;

import java.io.File;
import java.io.IOException;

import noNamespace.OrdersDocument;
import noNamespace.OrdersDocument.Orders.Order;

import org.apache.xmlbeans.XmlException;

public class ReadOrders {

	public ReadOrders() {
//		System.out.println("Test");
		
		File xmlFile = new File("ORD_20100414_0808_orders.xml");
//		File xmlFile = new File("test.xml");

		
		OrdersDocument ordsDoc = null;
		try {
			ordsDoc = OrdersDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println(ordsDoc.getOrders().getOrderArray()[0].getHeader().getNumber());
//		System.out.println(ordsDoc.validate());
		
		for (int i=0; i<ordsDoc.getOrders().getOrderArray().length;i++)
		{
			OrdersDocument ords2 = OrdersDocument.Factory.newInstance();
			Order ord = ords2.addNewOrders().addNewOrder();
			ord.setDetails(ordsDoc.getOrders().getOrderArray()[i].getDetails());
			ord.setHeader(ordsDoc.getOrders().getOrderArray()[i].getHeader());

//			System.out.println(i + "  " + ords2.validate());
			if (!ords2.validate())
			{
				System.out.println(ords2.getOrders().getOrderArray()[0].getHeader().getNumber());
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ReadOrders();
	}

}
