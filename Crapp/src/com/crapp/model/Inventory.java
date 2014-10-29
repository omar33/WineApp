package com.crapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Inventory 
{
	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField(canBeNull=false)
	private String wineName;
	
	@DatabaseField(canBeNull=false)
	private double price;

	@DatabaseField(canBeNull=false)
	private int quantity;
	
	@DatabaseField(canBeNull=false)
	private String wineType;
	
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private UserInfo user;
	
	
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setWineName( String wineName) {
		this.wineName = wineName;
	}

	public String getWineName() {
		return wineName;
	}
	
	public void setPrice( double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}
	
	public void setQuantity( int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setWineType( String wineType) {
		this.wineType = wineType;
	}

	public String getWineType() {
		return wineType;
	}
	
	public void setUser(UserInfo user) {
		this.user = user;
	}

	public UserInfo getUser() {
		return user;
	}
}
