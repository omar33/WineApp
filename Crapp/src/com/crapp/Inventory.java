package com.crapp;




import com.crapp.R;
import com.crapp.SimpleGestureFilter.SimpleGestureListener;

import android.app.TabActivity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;




import android.widget.GridView;

import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;



@SuppressWarnings("deprecation")
public class Inventory extends TabActivity implements SimpleGestureListener{
	GridView grid;
	TextView txtTest;

	private SimpleGestureFilter detector;

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		detector = new SimpleGestureFilter(this,this);
		setContentView(R.layout.inventory_background);
		SetupTabs();

	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(1, 1, 1, "Add Wine");
		return true;
	}



	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 1:
			String username= getIntent().getExtras().getString(Constants.userName);
			Intent addIntent=new Intent(this,Inventory_AddWine.class);
			addIntent.putExtra(Constants.userName,username);
			startActivity(addIntent);

			break;
		}
		super.onOptionsItemSelected(item);
		return false;
	}

	void SetupTabs()
	{
		//!!
		String username= getIntent().getExtras().getString(Constants.userName);

		TabHost host=getTabHost();

		TabHost.TabSpec spec=host.newTabSpec("tag1");

		Intent in1=new Intent(this, GridList.class);
		in1.putExtra(Constants.userName,username);
		in1.putExtra("WineType", "Dessert Wine");
		Log.d("for","sure");
		spec.setIndicator("Wines");

		spec.setContent(in1);

		TabHost.TabSpec spec2=host.newTabSpec("tag2");
		Intent in2=new Intent(this, Inventory_AddWine.class);
		in2.putExtra(Constants.userName,username);
		spec2.setIndicator("Add Wines");
		
		spec2.setContent(in2);


		host.addTab(spec);
		host.addTab(spec2);


	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me)
	{
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) 
	{
		String str = "";
		switch (direction) 
		{
		case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
		break;
		case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
		break;
		case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
		break;
		case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
		break;
		}

		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

		if(str == "Swipe Right")
		{
			finish();
			//Intent intentHomePage=new Intent(getApplicationContext(),HomePage.class);
			//startActivity(intentHomePage);
		}
	}

	@Override
	public void onDoubleTap() 
	{
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

}