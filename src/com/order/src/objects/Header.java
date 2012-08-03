package com.order.src.objects;

public class Header {

	private String no;
	private String number;
	private String refNo;
	private String poNo;
	private String shipOn;
	private String comment;
	private String contact;
	private String shipVia;
	private String service;
	private SoldTo soldTo;
	private ShipTo shipTo;
	private String invoice;
	
	public String getComment() {
		return comment;
	}
	public String getContact() {
		return contact;
	}
	public String getInvoice() {
		return invoice;
	}
	public String getNumber() {
		return number;
	}
	public String getPoNo() {
		return poNo;
	}
	public String getRefNo() {
		return refNo;
	}
	public String getService() {
		return service;
	}
	public String getShipOn() {
		return shipOn;
	}
	public ShipTo getShipTo() {
		return shipTo;
	}
	public String getShipVia() {
		return shipVia;
	}
	public SoldTo getSoldTo() {
		return soldTo;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setService(String service) {
		this.service = service;
	}
	public void setShipOn(String shipOn) {
		this.shipOn = shipOn;
	}
	public void setShipTo(ShipTo shipTo) {
		this.shipTo = shipTo;
	}
	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}
	public void setSoldTo(SoldTo soldTo) {
		this.soldTo = soldTo;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
		
}
