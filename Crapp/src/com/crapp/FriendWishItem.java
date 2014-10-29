package com.crapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.WishItem;
import com.crapp.model.WishList;

public class FriendWishItem extends Activity {
	ListView listView;
	WishList wishList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.friendwishitem, null);
        listView = (ListView) contentView.findViewById(R.id.frienditems);
        
        setContentView(contentView);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		int wishListId = getIntent().getExtras().getInt(Constants.keyWishListId);
		wishList = DatabaseManager.getInstance().getWishListWithId(wishListId);
		Log.i("Wine WishList","wishList="+wishList+" wishListId="+wishListId);
		setupListView();
		setTitle("Wine Wish list '"+wishList.getName()+"'");
	}
	
	private void setupListView() {
		if (null != wishList) 
		{
			final List<WishItem> wishItems = wishList.getItems();
			//List<String> titles = new ArrayList<String>();
			ArrayList<HashMap<String,String>> titles = new ArrayList<HashMap<String,String>>();
			for (WishItem wishItem : wishItems) 
			{
				
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(Constants.first_Column,String.valueOf(wishItem.getName()));
				temp.put(Constants.second_Column, String.valueOf(wishItem.getPrice()));
				titles.add(temp);
				//titles.add(wishItem.getName());
			}
			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
			listviewAdapter adapter = new listviewAdapter(this, titles);
			listView.setAdapter(adapter);
			final Activity activity = this;
			listView.setOnItemClickListener(new OnItemClickListener() 
			{
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					WishItem item = wishItems.get(position);
					Intent intent = new Intent(activity,AddWishItemActivity.class);
					intent.putExtra(Constants.keyWishItemId, item.getId());
					startActivity(intent);
				}
			});
		}
	}

}