package com.bankingsystem.dto;

import java.util.Date;

public class TransactionResponse {

    private String type;
    private double amount;
    private Date date;
    private String direction;
    
 // getters setters
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
   
}
