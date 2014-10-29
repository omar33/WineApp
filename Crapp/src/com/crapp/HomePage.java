package com.crapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.crapp.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.FBConnect;
import com.crapp.model.Friends;
import com.crapp.model.UserInfo;

public class HomePage extends Activity implements SimpleGestureListener{
    private ImageButton wine_review;
	private ImageButton wine_calc;
	private ImageButton wine_inventory;
	private ImageButton wine_list;
	private ImageButton logoff;
	private ImageButton friendsbt;
	private SimpleGestureFilter detector;
	UserInfo userInfo;
	String userName;
	String friendName;
	//TextView view;
	//PopupWindow popupWindow;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        logoff=(ImageButton)findViewById(R.id.lofoff);
        friendsbt= (ImageButton)findViewById(R.id.friendsbt);
        wine_review=(ImageButton)findViewById(R.id.wineReview);
        wine_calc=(ImageButton)findViewById(R.id.wineCalc);
        wine_inventory=(ImageButton)findViewById(R.id.wineInventory);
        wine_list=(ImageButton)findViewById(R.id.wishList);
        detector = new SimpleGestureFilter(this,this);        
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        
        //String curyear = formattedDate.substring(6);
        String curmonth = formattedDate.substring(3,5);
        String curday = formattedDate.substring(0,2);
        
        userName= getIntent().getExtras().getString(Constants.userName);
        userInfo = DatabaseManager.getInstance().getUserInfoWithId(userName);
        List<Friends> friends = userInfo.getFriendsItems();
        Log.d("1","1");
        for(Friends fren : friends)
        {   
        	Log.d("2","2");
        	friendName = fren.getFriendUserName();
        	UserInfo friendUser = DatabaseManager.getInstance().getUserInfoWithId(friendName);
        	String birthdate = friendUser.getBirthDate();
        	//String year = birthdate.substring(6);
        	String month = birthdate.substring(2,4);
        	String day = birthdate.substring(0,2);
        	int intday = Integer.parseInt(day);
        	int curIntDay = Integer.parseInt(curday);
        	Log.d("curday",curday);
        	Log.d("curmonth",curmonth);
        	Log.d("day",day);
        	Log.d("month",month);
        	if( curmonth.equals(month) && (curIntDay-10)<=intday && intday<=curIntDay)
        	{
        	/*	Log.d("here,","jere");
        		LayoutInflater layoutInflater 
        	     = (LayoutInflater)getBaseContext()
        	      .getSystemService(LAYOUT_INFLATER_SERVICE);  
        	    View popupView = layoutInflater.inflate(R.layout.friendprompt, null);  
        	             popupWindow = new PopupWindow(
        	               popupView, 
        	               LayoutParams.WRAP_CONTENT,  
        	                     LayoutParams.WRAP_CONTENT);  
        	    Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        	    btnDismiss.setOnClickListener(new Button.OnClickListener(){

        	        @Override
        	        public void onClick(View v) {
        	         // TODO Auto-generated method stub
        	         popupWindow.dismiss();
        	        }});
        	    Button friendss = (Button)popupView.findViewById(R.id.grapWishList);
        	    friendss.setOnClickListener(new Button.OnClickListener(){

        	        @Override
        	        public void onClick(View v) {
        	         // TODO Auto-generated method stub
        	         popupWindow.dismiss();
        	        }});            
        	    
        	    //Log.d("here,","jerk");
        	    */
        		Toast.makeText(getApplicationContext(), friendName+" Birthday is less then 10 days away!!!", Toast.LENGTH_LONG).show();
        	    
        	}
        	//popupWindow.showAsDropDown(friendsbt, 50, -30);
        }
        
        
        
        logoff.setOnClickListener(new View.OnClickListener() {			
        	@Override
        	public void onClick(View v) {
        		finish();
        	}
        });
        
      friendsbt.setOnClickListener(new View.OnClickListener() {			
        	@Override
        	public void onClick(View v) 
        	{
        		//String username= getIntent().getExtras().getString(Constants.userName);
	            Intent intentFriendList=new Intent(getApplicationContext(),friendList.class);
	            intentFriendList.putExtra(Constants.userName,userName);
                startActivity(intentFriendList);
        	}
        });
        
        
        wine_review.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),FBConnect.class));
			}
		});
        
        wine_calc.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//String username= getIntent().getExtras().getString(Constants.userName);
	            Intent intentCalc=new Intent(getApplicationContext(),WineBuddy.class);
	            intentCalc.putExtra(Constants.userName,userName);
                startActivity(intentCalc);
			}
		});
        
        wine_inventory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String username= getIntent().getExtras().getString(Constants.userName);
				 Intent intentInventory=new Intent(getApplicationContext(),Inventory.class);
				 intentInventory.putExtra(Constants.userName,userName);
			//	 intentInventory.putExtra("WineType","Dessert Wine");
	             startActivity(intentInventory);
			}
		});
        wine_list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String username= getIntent().getExtras().getString(Constants.userName);
	            Intent intentWishList=new Intent(getApplicationContext(),WishListManagerActivity.class);
	            intentWishList.putExtra(Constants.userName,userName);
                startActivity(intentWishList);
                
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
       
       if(str == "Swipe Left") {
    	   startActivity(new Intent(getApplicationContext(),PlacesActivity.class));
       }
       
       if(str == "Swipe Right")
       {
           Intent intentSearchWine=new Intent(getApplicationContext(),WineList.class);
           startActivity(intentSearchWine);
       }
    }

    @Override
    public void onDoubleTap() 
    {
    	Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
    
    public void onBackPressed ()
    {
    
    }
}