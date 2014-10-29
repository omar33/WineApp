package com.crapp.userdb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.crapp.userdb.DatabaseHelper;
import com.crapp.model.Friends;
import com.crapp.model.Inventory;
import com.crapp.model.UserInfo;
import com.crapp.model.WishItem;
import com.crapp.model.WishList;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{

	// name of the database file 
	private static final String DATABASE_NAME = "UserInfoDB.sqlite";
	
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	
	// the DAO object we use to access the SimpleData table
	private Dao<Friends, String> friendsDao = null;
	private Dao<Inventory,Integer> inventoryDao = null;
	private Dao<UserInfo, String> userInfoDao = null;
	private Dao<WishList, Integer> wishListDao = null;
	private Dao<WishItem, Integer> wishItemDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) 
	{
		try {
			TableUtils.createTable(connectionSource, Inventory.class);
			TableUtils.createTable(connectionSource, Friends.class);
			TableUtils.createTable(connectionSource, UserInfo.class);
			TableUtils.createTable(connectionSource, WishList.class);
			TableUtils.createTable(connectionSource, WishItem.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) 
	{
		try {
			List<String> allSql = new ArrayList<String>(); 
			switch(oldVersion) 
			{
			  case 1: 
				  //allSql.add("alter table AdData add column `new_col` VARCHAR");
				  //allSql.add("alter table AdData add column `new_col2` VARCHAR");
			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
			throw new RuntimeException(e);
		}
		
	}
	
	public Dao<Friends, String> getFriendsDao() {
		if (null == friendsDao) {
			try {
				friendsDao = getDao(Friends.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return friendsDao;
	}
	
	public Dao<Inventory, Integer> getInventoryDao() {
		if (null == inventoryDao) {
			try {
				inventoryDao = getDao(Inventory.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return inventoryDao;
	}
	
	public Dao<UserInfo, String> getUserInfoDao() {
		if (null == userInfoDao) {
			try {
				userInfoDao = getDao(UserInfo.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return userInfoDao;
	}
	
	public Dao<WishList, Integer> getWishListDao() {
		if (null == wishListDao) {
			try {
				wishListDao = getDao(WishList.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return wishListDao;
	}
	
	public Dao<WishItem, Integer> getWishItemDao() {
		if (null == wishItemDao) {
			try {
				wishItemDao = getDao(WishItem.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return wishItemDao;
	}
}
