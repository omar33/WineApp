package com.crapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Friends 
{
	@DatabaseField(id=true)
	private String friendUserName;
	
	@DatabaseField(foreign=true,foreignAutoRefresh=true)
	private UserInfo user;
	
	public void setFriendUserName(String friendUserName) {
		this.friendUserName = friendUserName;
	}

	public String getFriendUserName() {
		return friendUserName;
	}
	
	public void setUser(UserInfo user) {
		this.user = user;
	}

	public UserInfo getUser() {
		return user;
	}
	
}
