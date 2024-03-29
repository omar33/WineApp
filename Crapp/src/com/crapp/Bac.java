package com.crapp;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.crapp.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Bac extends Activity{
	private TextView mTimeDisplay;
    private Button mPickTime;

    private int mHour;
    private int mMinute;
    private long milli;
    private long seconds;

    static final int TIME_DIALOG_ID = 0;
    
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bac);
             
        //--------------------Drinking Start Time---------------------
        mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
        mPickTime = (Button) findViewById(R.id.pickTime);
        
        mPickTime.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        
        // get the current time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        milli = c.getTimeInMillis();
        seconds = TimeUnit.MILLISECONDS.toSeconds(milli);
        
        
        // display the current date
        updateDisplay();
        //----------------------------------------------------------------
        
        //---------------------Current Drink Table------------------------
        
        //Moves current drink values from Tab Tracker to the BAC Tab
        final TextView totalBeer = (TextView) findViewById(R.id.bac_total_sparkling);
        totalBeer.setText(Integer.toString(WineTab.sparklingwine));
        final TextView totalWell = (TextView) findViewById(R.id.bac_total_table);
        totalWell.setText(Integer.toString(WineTab.tablewine));
        final TextView totalLiquor = (TextView) findViewById(R.id.bac_total_dry);
        totalLiquor.setText(Integer.toString(WineTab.drywine));
        final TextView totalBombs = (TextView) findViewById(R.id.bac_total_cabernet);
        totalBombs.setText(Integer.toString(WineTab.cabernet));
        final TextView totalCocktails = (TextView) findViewById(R.id.bac_total_barley);
        totalCocktails.setText(Integer.toString(WineTab.barleywine));
        
        //------------------------------------------------------------------
        
        //----------------------Calculate BAC--------------------------------
        final Button bacCalc = (Button) findViewById(R.id.bac_calculate);
        //final TextView bac = (TextView) findViewById(R.id.bac_bloodAlcoholContent);
        
        
        bacCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Gather Variables
            	CheckBox nextDay = (CheckBox) findViewById(R.id.nextDay);
            	RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioSex = (RadioButton) findViewById(selectedId);
                
                final Calendar c = Calendar.getInstance();
                int totalDiff = 0;
                if(nextDay.isChecked()){
                	int currentHour = c.get(Calendar.HOUR_OF_DAY) + 24;
                    int currentMinute = c.get(Calendar.MINUTE);
                    int hourDiff = (currentHour - mHour );
                    if (currentMinute < mMinute){
                    	currentMinute = currentMinute + 60;
                    }
                    int minDiff = (currentMinute - mMinute);
                    totalDiff = (minDiff + (60 * hourDiff));
                }
                else{
                	int currentHour = c.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = c.get(Calendar.MINUTE);
                    int hourDiff = (currentHour - mHour );
                    //Check to make sure they did not enter a time in the past
                    if (hourDiff < 0){
                    	Context con = getApplicationContext();
                    	Toast toast = Toast.makeText(con, "Please enter a time that is in the past", Toast.LENGTH_SHORT);
                    	toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                    	toast.show();
                    	return;
                    }
                    if (currentMinute < mMinute){
                    	currentMinute = currentMinute + 60;
                    }
                    int minDiff = (currentMinute - mMinute);
                    totalDiff = (minDiff + (60 * hourDiff));
                }
                
                EditText temp = (EditText) findViewById(R.id.bacWeight);
                String tempString = temp.getText().toString();
                if (tempString.matches("")){
                	Context con = getApplicationContext();
                	Toast toast = Toast.makeText(con, "Please enter a value between 100-400 for weight", Toast.LENGTH_SHORT);
                	toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                	toast.show();
                	return;
                }
                int weight = Integer.parseInt(tempString);
                
                //Check for weight restrictions
                if (weight < 100){
                	Context con = getApplicationContext();
                	Toast toast = Toast.makeText(con, "Please enter a value between 100-400 for weight", Toast.LENGTH_SHORT);
                	toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                	toast.show();
                	return;
                }else if(weight > 400){
                	Context con = getApplicationContext();
                	Toast toast = Toast.makeText(con, "Please enter a value between 100-400 for weight", Toast.LENGTH_SHORT);
                	toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                	toast.show();
                	return;
                }
                
                TextView bacView = (TextView) findViewById(R.id.bac_bloodAlcoholContent);
                
                //Algorithm used for men
                if(radioSex.getText().toString().equalsIgnoreCase("male")){
                	//Calculate BAC based on alcohol and weight, using .02% per drink
                	//Cocktails will count as 2 drinks, wells as .75 of a drink
                	
                	//Find proportionate weight factor based on .02% for a 200lb man
                	double weightFactor = (200/(double)weight);
                	double bac = 0.0;
                	bac = bac + ((double)WineTab.sparklingwine * (.02 * weightFactor));
                	bac = bac + ((double)WineTab.tablewine * (.02*.75* weightFactor));
                	bac = bac + ((double)WineTab.drywine * (.02 * weightFactor));
                	bac = bac + ((double)WineTab.cabernet * (.02 * weightFactor));
                	bac = bac + ((double)WineTab.barleywine * (.04 * weightFactor));
                	
                	//Finds the amount bac has decreased during drinking
                	//Subtracts .005% per 20 mins
                	double soberingFactor = ((double)totalDiff / 20.0);
                	double subtraction = (.005 * soberingFactor);
                	
                	bac = bac - subtraction;
                	if(bac<0)
                	{
                		bac=0;
                	}
                	updateBac(bacView, round(bac, 2, BigDecimal.ROUND_HALF_UP));
                }
                //Algorithm used for women
                else{
                	//Calculate BAC based on alcohol and weight, using .02% per drink
                	//Cocktails will count as 2 drinks, wells as .75 of a drink
                	
                	//Find proportionate weight factor based on .03% for a 140lb woman
                	double weightFactor = (140/(double)weight);
                	double bac = 0.0;
                	bac = bac + ((double)WineTab.sparklingwine * (.03 * weightFactor));
                	bac = bac + ((double)WineTab.tablewine * (.03*.75* weightFactor));
                	bac = bac + ((double)WineTab.drywine * (.03 * weightFactor));
                	bac = bac + ((double)WineTab.cabernet * (.03 * weightFactor));
                	bac = bac + ((double)WineTab.barleywine * (.05 * weightFactor));
                	
                	//Finds the amount bac has decreased during drinking
                	//Subtracts .005% per 20 mins
                	double soberingFactor = ((double)totalDiff / 20.0);
                	double subtraction = (.005 * soberingFactor);
                	
                	bac = bac - subtraction;
                	if(bac<0)
                	{
                		bac=0;
                	}
                	updateBac(bacView, round(bac, 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        });
    }
	
	// updates the time we display in the TextView
	private void updateDisplay() {
	    mTimeDisplay.setText(
	        new StringBuilder()
	                .append("Current Start Time -- ").append(pad(mHour)).append(":")
	                .append(pad(mMinute)));
	    
	    final TextView totalBeer = (TextView) findViewById(R.id.bac_total_sparkling);
        totalBeer.setText(Integer.toString(WineTab.sparklingwine));
        final TextView totalWell = (TextView) findViewById(R.id.bac_total_table);
        totalWell.setText(Integer.toString(WineTab.tablewine));
        final TextView totalLiquor = (TextView) findViewById(R.id.bac_total_dry);
        totalLiquor.setText(Integer.toString(WineTab.drywine));
        final TextView totalBombs = (TextView) findViewById(R.id.bac_total_cabernet);
        totalBombs.setText(Integer.toString(WineTab.cabernet));
        final TextView totalCocktails = (TextView) findViewById(R.id.bac_total_barley);
        totalCocktails.setText(Integer.toString(WineTab.barleywine));
	}
	
	private void updateBac(TextView t, double d){
		if (d < 0.08){ t.setText(
				new StringBuilder()
				.append("Estimated BAC: ")
				.append(Double.toString(d)));
		} else {
			t.setText(
					new StringBuilder()
					.append("Estimated BAC: ")
					.append(Double.toString(d))
					.append("                    dialing your local taxi service!"));
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ (619-444-4444)));
			startActivity(intent);
			
		}
				
	}
	
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case TIME_DIALOG_ID:
	        return new TimePickerDialog(this,
	                mTimeSetListener, mHour, mMinute, false);
	    }
	    return null;
	}
	
	// the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute; 
                updateDisplay();
            }
        };
        
    public static double round(double unrounded, int precision, int roundingMode){
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
	
        
    protected void onResume(){
    	super.onResume();
    	
    	final TextView totalBeer = (TextView) findViewById(R.id.bac_total_sparkling);
    	totalBeer.setText(Integer.toString(WineTab.sparklingwine));
        final TextView totalWell = (TextView) findViewById(R.id.bac_total_table);
        totalWell.setText(Integer.toString(WineTab.tablewine));
        final TextView totalLiquor = (TextView) findViewById(R.id.bac_total_dry);
        totalLiquor.setText(Integer.toString(WineTab.drywine));
        final TextView totalBombs = (TextView) findViewById(R.id.bac_total_cabernet);
        totalBombs.setText(Integer.toString(WineTab.cabernet));
        final TextView totalCocktails = (TextView) findViewById(R.id.bac_total_barley);
        totalCocktails.setText(Integer.toString(WineTab.barleywine));
    }
}
