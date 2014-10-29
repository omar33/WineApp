package com.crapp;

import com.crapp.R;
import com.crapp.SimpleGestureFilter.SimpleGestureListener;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WineList extends ListActivity implements SimpleGestureListener {

	protected EditText searchText;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;
	private SimpleGestureFilter detector;

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        searchText = (EditText) findViewById(R.id.searchText);
    	db = (new DatabaseHelper2(this)).getWritableDatabase();
    	
    	// swipe moton detector
        detector = new SimpleGestureFilter(this,this);
    }
    
    public void onListItemClick(ListView parent, View view, int position, long id) {
    	Intent intent = new Intent(this, EmployeeDetails.class);
    	Cursor cursor = (Cursor) adapter.getItem(position);
    	intent.putExtra("WINE_ID", cursor.getInt(cursor.getColumnIndex("_id")));
    	startActivity(intent);
    }
    
    @SuppressWarnings("deprecation")
	public void search(View view) {
    	// || is the concatenation operation in SQLite
		cursor = db.rawQuery("SELECT _id, wineName, wineTitle, location FROM employee WHERE wineName || ' ' || wineTitle LIKE ?", 
						new String[]{"%" + searchText.getText().toString() + "%"});
		adapter = new SimpleCursorAdapter(
				this, 
				R.layout.wine_list_item, 
				cursor, 
				new String[] {"wineName", "wineTitle", "location"}, 
				new int[] {R.id.wineName, R.id.wineTitle, R.id.location});
		setListAdapter(adapter);
    }
    
    
  //ADDED ON METHOES FOR SWIPE!!!!
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