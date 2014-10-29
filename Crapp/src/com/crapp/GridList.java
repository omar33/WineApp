package com.crapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.crapp.Inventory_Alerts;
import com.crapp.GridList;

import com.crapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.UserInfo;
import com.crapp.model.Inventory;
import com.crapp.listviewAdapter;

public class GridList extends Activity{
	Spinner spinDept1;
	UserInfo userInfo;
	ListView lview;
	List<Inventory> inventoryList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory_gridview);

		spinDept1 = (Spinner)findViewById(R.id.spinType1);
		lview = (ListView) findViewById(R.id.listview);

		inventoryList = new ArrayList<Inventory>();

		spinDept1.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				populateList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub

			}
		}
		);

		lview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) 
			{
				Inventory item = inventoryList.get(position);
				AlertDialog diag = Inventory_Alerts.ShowEditDialog(GridList.this, item);
				diag.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) 
					{
						populateList();
					}
				});
				diag.show();
			}
		}


		);
	}

	public void populateList()
	{
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		String selectedType= String.valueOf(spinDept1.getSelectedItem());
		String username = getIntent().getExtras().getString(Constants.userName);
		userInfo = DatabaseManager.getInstance().getUserInfoWithId(username);

		List<Inventory> items = userInfo.getInventoryItems();
		inventoryList.clear();
		for (Inventory inventoryItem : items) 
		{   
			if((inventoryItem.getWineType()).equals(selectedType))
			{
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put(Constants.first_Column,String.valueOf(inventoryItem.getWineName()));
				temp.put(Constants.second_Column, String.valueOf(inventoryItem.getQuantity()));
				list.add(temp);
				inventoryList.add(inventoryItem);
			}		

		}
		int c = list.size();
		Log.d("!!  !!",String.valueOf(c));
		listviewAdapter adapter = new listviewAdapter(this, list);
		lview.setAdapter(adapter);
	}

	public void onResume()
	{
		super.onResume();
		populateList();
	}


}

