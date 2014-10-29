package com.crapp.userdb;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.crapp.model.Friends;
import com.crapp.model.Inventory;
import com.crapp.model.UserInfo;
import com.crapp.model.WishItem;
import com.crapp.model.WishList;


public class DatabaseManager 
{
	static private DatabaseManager instance;

	static public void init(Context ctx) {
		if (null==instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	static public DatabaseManager getInstance() {
		return instance;
	}
	
	private DatabaseHelper helper;
	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}
	
	private DatabaseHelper getHelper() {
		return helper;
	}
	
	////////////////////////////////////USERINFO///////////////////////////////////////////////
	public List<UserInfo> getAllUserInfos() {
		List<UserInfo> userInfo = null;
		try {
			userInfo = getHelper().getUserInfoDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	
	public UserInfo getUserInfoWithId(String userInfoId) {
		UserInfo userInfo = null;
		try {
			userInfo = getHelper().getUserInfoDao().queryForId(userInfoId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	
	public void addUserInfo(UserInfo l) {
		try {
			getHelper().getUserInfoDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUserInfo(UserInfo userInfo) {
		try {
			getHelper().getUserInfoDao().delete(userInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshUserInfo(UserInfo userInfo) {
		try {
			getHelper().getUserInfoDao().refresh(userInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateWishList(UserInfo userInfo) {
		try {
			getHelper().getUserInfoDao().update(userInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////INVENTORY/////////////////////////////////////////
	
	public Inventory getInventoryWithId(int inventoryId) {
		Inventory inventory = null;
		try {
			inventory = getHelper().getInventoryDao().queryForId(inventoryId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventory;
	}
	
	public Inventory newInventory() {
		Inventory inventory = new Inventory();
		try {
			getHelper().getInventoryDao().create(inventory);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inventory;
	}
	
	public void deleteInventory(Inventory inventory) {
		try {
			getHelper().getInventoryDao().delete(inventory);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void updateInventory(Inventory item) {
		try {
			getHelper().getInventoryDao().update(item);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addInventory(Inventory l) {
		try {
			getHelper().getInventoryDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////Friends////////////////////////////
	
	public Friends getFriendsWithId(String friendId) {
		Friends friend = null;
		try {
			friend = getHelper().getFriendsDao().queryForId(friendId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friend;
	}
	
	public Friends newFriend() {
		Friends friend = new Friends();
		try {
			getHelper().getFriendsDao().create(friend);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friend;
	}
	
	public void deleteFriend(Friends friend) {
		try {
			getHelper().getFriendsDao().delete(friend);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void addFriends(Friends l) {
		try {
			getHelper().getFriendsDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//////////////////////////////WISHLIST//////////////////////
	
	public List<WishList> getAllWishLists() {
		List<WishList> wishLists = null;
		try {
			wishLists = getHelper().getWishListDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishLists;
	}

	public void addWishList(WishList l) {
		try {
			getHelper().getWishListDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public WishList getWishListWithId(int wishListId) {
		WishList wishList = null;
		try {
			wishList = getHelper().getWishListDao().queryForId(wishListId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishList;
	}
	
	public void deleteWishList(WishList wishList) {
		try {
			getHelper().getWishListDao().delete(wishList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshWishList(WishList wishList) {
		try {
			getHelper().getWishListDao().refresh(wishList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateWishList(WishList wishList) {
		try {
			getHelper().getWishListDao().update(wishList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	///////////////////////WISHITEM/////////////////////////
	
	public WishItem getWishItemWithId(int wishItemId) {
		WishItem wishList = null;
		try {
			wishList = getHelper().getWishItemDao().queryForId(wishItemId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishList;
	}

	public WishItem newWishItem() {
		WishItem wishItem = new WishItem();
		try {
			getHelper().getWishItemDao().create(wishItem);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishItem;
	}
	
	public void deleteWishItem(WishItem wishItem) {
		try {
			getHelper().getWishItemDao().delete(wishItem);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public void updateWishItem(WishItem item) {
		try {
			getHelper().getWishItemDao().update(item);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public List<WishItem> getAllWishItems() {
		List<WishItem> wishItems = null;
		try {
			wishItems = getHelper().getWishItemDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishItems;
	}
	
	public void addWishItem(WishItem l) {
		try {
			getHelper().getWishItemDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
