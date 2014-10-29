package com.crapp;

import com.crapp.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TabTracker extends Activity{
	
	//shared pref
	public static final String PREFS_COUNT = "MyPrefsFile";
	
	//onCreate method
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winetab);
        
        //sparklingwine button
        final Button buttonSparkling = (Button) findViewById(R.id.button_sparkling);
        final TextView totalSparkling = (TextView) findViewById(R.id.total_sparkling);
        
        buttonSparkling.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	int num_sparkling = Integer.parseInt((String)totalSparkling.getText());
            	num_sparkling++;
            	WineTab.sparklingwine=num_sparkling;
            	totalSparkling.setText(Integer.toString(num_sparkling));
            }
        });
        
        //tablewine button
        final Button buttonTable = (Button) findViewById(R.id.button_table);
        final TextView totalTable = (TextView) findViewById(R.id.total_table);
        
        buttonTable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	int num_table = Integer.parseInt((String)totalTable.getText());
            	num_table++;
            	WineTab.tablewine = num_table;
            	totalTable.setText(Integer.toString(num_table));
            }
        });
        
        //drywine button
        final Button buttonDry = (Button) findViewById(R.id.button_dry);
        final TextView totalDry = (TextView) findViewById(R.id.total_dry);
        
        buttonDry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	int num_dry = Integer.parseInt((String)totalDry.getText());
            	num_dry++;
            	WineTab.drywine = num_dry;
            	totalDry.setText(Integer.toString(num_dry));
            }
        });
        
        //bomb button
        final Button buttonCabernet = (Button) findViewById(R.id.button_cabernet);
        final TextView totalCabernet = (TextView) findViewById(R.id.total_cabernet);
        
        buttonCabernet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	int num_cabernet = Integer.parseInt((String)totalCabernet.getText());
            	num_cabernet++;
            	WineTab.cabernet = num_cabernet;
            	totalCabernet.setText(Integer.toString(num_cabernet));
            }
        });
        
        //barleywine button
        final Button buttonBarley = (Button) findViewById(R.id.button_barley);
        final TextView totalBarley = (TextView) findViewById(R.id.total_barley);
        
        buttonBarley.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	int num_barley = Integer.parseInt((String)totalBarley.getText());
            	num_barley++;
            	WineTab.barleywine = num_barley;
            	totalBarley.setText(Integer.toString(num_barley));
            }
        });
        
        //clear button
        final Button buttonClear = (Button) findViewById(R.id.button_clear);
        
        buttonClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	totalSparkling.setText("0");
            	totalTable.setText("0");
            	totalDry.setText("0");
            	totalCabernet.setText("0");
            	totalBarley.setText("0");
            	
            	WineTab.sparklingwine = 0;
            	WineTab.tablewine = 0;
            	WineTab.drywine = 0;
            	WineTab.cabernet = 0;
            	WineTab.barleywine = 0;
            }
        });
        
    }
	//on pause.. on resume
	
	//on pause preserves the current drink count in a shared preference
	@Override
	protected void onPause()
	{
		super.onPause();
		
		SharedPreferences winePrefs = getSharedPreferences(PREFS_COUNT,0);
		
		SharedPreferences.Editor ed = winePrefs.edit();
		ed.putInt("sparklingwine", WineTab.sparklingwine);
		ed.putInt("tablewine", WineTab.tablewine);
		ed.putInt("drywine", WineTab.drywine);
		ed.putInt("cabernet", WineTab.cabernet);
		ed.putInt("barleywine", WineTab.barleywine);
		
		ed.commit();
	}
	
	//on Resume restores the previous drink count total
	@Override
	protected void onResume()
	{
		super.onResume();
		
		SharedPreferences winePrefs = getSharedPreferences(PREFS_COUNT,0);
		
		//find the text views
		final TextView totalSparkling = (TextView) findViewById(R.id.total_sparkling);
		final TextView totalTable = (TextView) findViewById(R.id.total_table);
		final TextView totalDry = (TextView) findViewById(R.id.total_dry);
		final TextView totalCabernet = (TextView) findViewById(R.id.total_cabernet);
		final TextView totalBarley = (TextView) findViewById(R.id.total_barley);
		
		//get the previous drink counts
		int sparklingCount = winePrefs.getInt("sparklingwine",0);
		int tableCount = winePrefs.getInt("tablewine",0);
		int dryCount = winePrefs.getInt("drywine",0);
		int cabernetCount = winePrefs.getInt("cabernet",0);
		int barleyCount = winePrefs.getInt("barleywine",0);
		
		//reset the labels to the previous values
		totalSparkling.setText(Integer.toString(sparklingCount));
    	totalTable.setText(Integer.toString(tableCount));
    	totalDry.setText(Integer.toString(dryCount));
    	totalCabernet.setText(Integer.toString(cabernetCount));
    	totalBarley.setText(Integer.toString(barleyCount));
    	
    	
    	//update the WineTab fields
    	WineTab.sparklingwine = sparklingCount;
    	WineTab.tablewine = tableCount;
    	WineTab.drywine = dryCount;
    	WineTab.cabernet = cabernetCount;
    	WineTab.barleywine = barleyCount;
    	
    	
		
				
	}
	
	
}
