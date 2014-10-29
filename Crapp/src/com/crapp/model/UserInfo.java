package com.crapp.model;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserInfo 
{
	@DatabaseField(id=true)
	private String username;
	
	@DatabaseField(canBeNull=false)
	private String password;
	
	@DatabaseField(canBeNull=false)
	private String birthDate;
	
	@DatabaseField(canBeNull=false)
	private String securityQuestion;
	
	@DatabaseField(canBeNull=false)
	private String securityAnswer;
	
	@ForeignCollectionField
	private ForeignCollection<Inventory> inventoryItems;
	
	@ForeignCollectionField
	private ForeignCollection<WishList> wishListItems;
	
	@ForeignCollectionField
	private ForeignCollection<Friends> friendsItems;
	
	
	public UserInfo() {
        
    }
	
	public UserInfo(String username, String password, String securityQuestion, String securityAnswer, String date)
	{
	   this.username = username;
	   this.password = password;
	   this.securityQuestion = securityQuestion;
	   this.securityAnswer = securityAnswer;
	   this.birthDate = date;
	   
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthDate() {
		return birthDate;
	}
	
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}
	
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}
	
	public void setInventoryItems(ForeignCollection<Inventory> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	public List<Inventory> getInventoryItems() {
		ArrayList<Inventory> itemList = new ArrayList<Inventory>();
		for (Inventory item : inventoryItems) {
			itemList.add(item);
		}
		return itemList;
	}
	
	public void setWishListItems(ForeignCollection<WishList> wishListItems) {
		this.wishListItems = wishListItems;
	}

	public List<WishList> getWishListItems() {
		ArrayList<WishList> itemList = new ArrayList<WishList>();
		for (WishList item : wishListItems) {
			itemList.add(item);
		}
		return itemList;
	}
	
	public void setFriendsItems(ForeignCollection<Friends> friendsItems) {
		this.friendsItems = friendsItems;
	}

	public List<Friends> getFriendsItems() {
		ArrayList<Friends> itemList = new ArrayList<Friends>();
		for (Friends item : friendsItems) {
			itemList.add(item);
		}
		return itemList;
	}
}
