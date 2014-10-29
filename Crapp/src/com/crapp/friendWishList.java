package com.crapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.crapp.userdb.DatabaseManager;
import com.crapp.SimpleGestureFilter.SimpleGestureListener;
import com.crapp.model.WishList;
import com.crapp.model.UserInfo;

public class friendWishList extends Activity implements SimpleGestureListener{
	ListView listView;
	private SimpleGestureFilter detector;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager.init(this);
        detector = new SimpleGestureFilter(this,this);

        ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.friendwishlist, null);
        listView = (ListView) contentView.findViewById(R.id.friendWishLists);

        setContentView(contentView);
    }

    @Override
    protected void onStart() {
    	super.onStart();
        setupListView(listView);
    }

    private void setupListView(ListView lv) {
    	final String friendUsername = getIntent().getExtras().getString(Constants.friendsUserName);
    	final UserInfo userInfoFriend = DatabaseManager.getInstance().getUserInfoWithId(friendUsername);
    	final List<WishList> wishLists = userInfoFriend.getWishListItems();
    	
    	List<String> titles = new ArrayList<String>();
    	for (WishList wl : wishLists) {
    		titles.add(wl.getName());
    	}

    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
    	lv.setAdapter(adapter);

    	final Activity activity = this;
    	lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				WishList wishList = wishLists.get(position);
				Intent intent = new Intent(activity, FriendWishItem.class);
				//intent.putExtra(Constants.userName, username);
				intent.putExtra(Constants.keyWishListId, wishList.getId());
				startActivity(intent);
			}
		});
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
       
       if(str == "Swipe Right")
       {
           finish();
       }
    }

    @Override
    public void onDoubleTap() 
    {
    	Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}