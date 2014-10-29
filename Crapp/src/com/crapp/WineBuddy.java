package com.crapp;

import com.crapp.R;
import com.crapp.SimpleGestureFilter.SimpleGestureListener;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class WineBuddy extends TabActivity implements SimpleGestureListener{
	
	private SimpleGestureFilter detector;
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        detector = new SimpleGestureFilter(this,this);
        
        setContentView(R.layout.wine_calculator);
        
        //hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, TabTracker.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("Wine tab").setIndicator("wines tab",
                res.getDrawable(R.drawable.ic_launcher))
               .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        
       
        
     // Do the same for the other tabs
        intent = new Intent().setClass(this, Bac.class);
        spec = tabHost.newTabSpec("BAC").setIndicator("BAC",
                res.getDrawable(R.drawable.ic_launcher))
                .setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(3);
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
    	  /* String username = getIntent().getExtras().getString(Constants.userName);
           Intent intentHomePage=new Intent(getApplicationContext(),HomePage.class);
           intentHomePage.putExtra(Constants.userName, username);
           startActivity(intentHomePage);*/
       }
    }

    @Override
    public void onDoubleTap() 
    {
    	Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}