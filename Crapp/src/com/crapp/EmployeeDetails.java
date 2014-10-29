package com.crapp;

import java.util.ArrayList;
import java.util.List;

import com.crapp.R;
import com.crapp.SimpleGestureFilter.SimpleGestureListener;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeDetails extends ListActivity implements SimpleGestureListener{

	protected TextView wineNameText;
	protected TextView titleText;
	protected List<WineAction> actions;
	protected WineActionAdapter adapter;
	protected int wineId;
	private SimpleGestureFilter detector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_details);
		detector = new SimpleGestureFilter(this,this);

		wineId = getIntent().getIntExtra("WINE_ID", 0);
		SQLiteDatabase db = (new DatabaseHelper2(this)).getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT emp._id, emp.wineName, emp.wineTitle, emp.location, emp.description, emp.foodParing, emp.year, emp.price FROM employee emp WHERE emp._id = ?", 
				new String[]{""+wineId});

		if (cursor.getCount() == 1)
		{
			cursor.moveToFirst();

			wineNameText = (TextView) findViewById(R.id.fullWine);
			wineNameText.setText(cursor.getString(cursor.getColumnIndex("wineName")) + " " + cursor.getString(cursor.getColumnIndex("wineTitle")));

			titleText = (TextView) findViewById(R.id.location);
			titleText.setText(cursor.getString(cursor.getColumnIndex("location")));

			actions = new ArrayList<WineAction>();


			String description = cursor.getString(cursor.getColumnIndex("description"));
			if (description != null) {
				actions.add(new WineAction("Description", description));
			}



			String foodParing = cursor.getString(cursor.getColumnIndex("foodParing"));
			if (foodParing != null) {
				actions.add(new WineAction("Food Pairing", foodParing));
			}

			String year = cursor.getString(cursor.getColumnIndex("year"));
			if (year != null) {
				actions.add(new WineAction("Year", year));
			}

			String price = cursor.getString(cursor.getColumnIndex("price"));
			if (price != null) {
				actions.add(new WineAction("Price", price));
			}
			adapter = new WineActionAdapter();
			setListAdapter(adapter);
		}

	}

	class WineActionAdapter extends ArrayAdapter<WineAction> {

		WineActionAdapter() {
			super(EmployeeDetails.this, R.layout.action_list_item, actions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WineAction action = actions.get(position);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.action_list_item, parent, false);
			TextView label = (TextView) view.findViewById(R.id.label);
			label.setText(action.getLabel());
			TextView data = (TextView) view.findViewById(R.id.data);
			data.setText(action.getData());
			return view;
		}

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

		if(str == "Swipe Left")
		{
			finish();// finishActivity(0);
		}
	}

	@Override
	public void onDoubleTap() 
	{
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

}