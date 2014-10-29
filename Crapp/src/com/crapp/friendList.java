package com.crapp;

import java.util.ArrayList;
import java.util.List;

import com.crapp.model.Friends;
import com.crapp.model.UserInfo;
import com.crapp.userdb.DatabaseManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class friendList extends Activity
{
	Button addFriend;
	UserInfo userInfo;
	String userName;
	ListView listView;
	ArrayAdapter<String> adapter;
	List<Friends> items;
	String friendName;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		
		listView = (ListView) findViewById(R.id.friendlistview);
		userName =  getIntent().getExtras().getString(Constants.userName);
		userInfo = DatabaseManager.getInstance().getUserInfoWithId(userName);
		final EditText name=(EditText)findViewById(R.id.friendUserName);
		addFriend = (Button)findViewById(R.id.addFriend);
		
		addFriend.setOnClickListener(new View.OnClickListener() 
		{
	        public void onClick(View v)
	        {
	        	friendName =name.getText().toString();
	        	List<Friends> lf  = userInfo.getFriendsItems();
	        	for (Friends frien : lf) 
	        	{
	        		if(frien.getFriendUserName() == friendName)
	        		{
	        			Toast.makeText(friendList.this, "You already Added this friend", Toast.LENGTH_LONG).show();
	        			return;
	        		}
	        	}
	        	if( DatabaseManager.getInstance().getUserInfoWithId(friendName) == null)
		    	{
		    		Toast.makeText(friendList.this, "Username does not exit", Toast.LENGTH_LONG).show();
		        	return;
		    	}
	        	else if( friendName.equals(userName))
	        	{
	        		Toast.makeText(friendList.this, "Trying to Add Yourself As A friend? \n Thats sad )=", Toast.LENGTH_LONG).show();
		        	return;
	        	}
		    	else
		    	{
		    		Friends l = new Friends();
		    		l.setFriendUserName(friendName);
		    		l.setUser(userInfo);
		    		DatabaseManager.getInstance().addFriends(l);
		    		populate();
		    		Toast.makeText(friendList.this, "Friend Added Succesfully", Toast.LENGTH_LONG).show();
		    	}
	        }
		});  
	/*	final List<Friends> items = userInfo.getFriendsItems();
		List<String> titles = new ArrayList<String>();
		for (Friends item : items) 
		{
			titles.add(item.getFriendUserName());
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
		listView.setAdapter(adapter);*/
		populate();
		
		final Activity activity = this;
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Friends item = items.get(position);
				String frenName = item.getFriendUserName();
				Intent intent = new Intent(activity,friendWishList.class);
				intent.putExtra(Constants.friendsUserName, frenName);
				startActivity(intent);
			}
		});
	}
	
	
	public void populate()
	{
		Log.d("user",userInfo.getUsername());
		items = userInfo.getFriendsItems();
		List<String> titles = new ArrayList<String>();
		for (Friends item : items) 
		{
			titles.add(item.getFriendUserName());
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
		listView.setAdapter(adapter);
	
	}
}
