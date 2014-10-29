package com.crapp;

import java.util.ArrayList;
import java.util.HashMap;

import com.crapp.R;

import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlacesActivity extends FragmentActivity {

	private AlertDialogManager alert = new AlertDialogManager();
	private ArrayList<HashMap<String, String>> placesListItems;
	private DialogFragment fragment;
	private GooglePlaces googlePlaces;
	private GPSTracker gps;
	private ListView lv;
	private LoadPlaces placeList;
	private PlacesList nearPlaces;
	private ProgressDialog pDialog;
	private double latitude, longitude, currLat, currLng;
	private int radius = 10000;
	
	// KEY strings
	public static String KEY_DISTANCE = "distance"; // distance of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_activity);
		
		// initialize items
		gps = new GPSTracker(this);
		placesListItems = new ArrayList<HashMap<String,String>>();
		
		// getting ListView
		lv = (ListView) findViewById(R.id.list);
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // starting new intent
                Intent in = new Intent(getApplicationContext(), SinglePlaceActivity.class);
                
                // sending place reference id to single place activity
                // place reference id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                in.putExtra("latitude", currLat);
				in.putExtra("longitude", currLng);
                startActivity(in);
            }
        });
		
		if(!haveInternet(getApplicationContext())) {
        	fragment = new InternetDialog();
            fragment.show(getSupportFragmentManager(), "network");
        } else if (gps.canGetLocation()) {
			currLat = gps.getLatitude();
			currLng = gps.getLongitude();
			Log.d("Current location", "latitude:" + currLat + ", longitude: " + currLng);
			
			// calling background Async task to load Google Places
			// After getting places from Google all the data is shown in listview
			placeList = new LoadPlaces();
			placeList.execute();
		} else gps.showSettingsAlert();
	}
	
	public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return false;
        return true;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.places, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.radius:
				fragment = new RadiusPickerDialog();
				fragment.show(getSupportFragmentManager(), "number");
				break;
			case R.id.map_mode:
				if(haveInternet(getApplicationContext())) {
					if(gps.canGetLocation()) {
						Intent i = new Intent(getApplicationContext(), PlacesMapActivity.class);
						i.putExtra("near_places", nearPlaces);
						i.putExtra("latitude", latitude);
						i.putExtra("longitude", longitude);
						startActivity(i);
						break;
					}
					else {
						gps.showSettingsAlert();
						break;
					}
				}
			case R.id.action_refresh:
				setRadius(radius);
			default:
				break;
	    }
	    return true;
	}

	@Override
    protected void onPause() {
        super.onPause();

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if(!haveInternet(getApplicationContext())) {
        	if(fragment != null)
        		fragment.dismiss();
        	fragment = new InternetDialog();
            fragment.show(getSupportFragmentManager(), "network");
        }
    }
    
    public int getRadius() {
    	return this.radius;
    }
    
    public void setRadius(int x) {
    	this.radius = x;
    	if(haveInternet(getApplicationContext())) {
    		if(gps.canGetLocation()) {
    			try {
    				((BaseAdapter) lv.getAdapter()).notifyDataSetInvalidated();
    			} catch(NullPointerException e) {
    			}
    			currLat = gps.getLatitude();
    			currLng = gps.getLongitude();
    			lv.setAdapter(null);
    			lv.invalidate();
    			placeList = null;
    			nearPlaces = null;
    			lv.invalidateViews();
    			placeList = new LoadPlaces();
    			placeList.execute();
    		} else {
    			gps.showSettingsAlert();
    		}
    	}
    }
    
    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
		float[] result=new float[1];
		Location.distanceBetween (lat1, lng1, lat2, lng2, result);
		return (double)result[0]/1000;
	}
    
    // Background Async Task to Load Google places
	class LoadPlaces extends AsyncTask<String, String, String> {

		// Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlacesActivity.this);
			pDialog.setMessage(Html.fromHtml("Loading nearby places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// getting Places JSON
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			try {
				// Separate place types by PIPE symbol "|"
				// get nearest places
				nearPlaces = null;
				nearPlaces = googlePlaces.search(gps.getLatitude(), gps.getLongitude(), radius);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// get JSON response status
					String status = nearPlaces.status;
					
					// check for all possible status
					if(status.equals("OK")){
						// successfully got places details
						if (nearPlaces.results != null) {
							placesListItems.clear();
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								latitude = p.geometry.location.lat;
								longitude = p.geometry.location.lng;
								double distance = distFrom(latitude, longitude, currLat, currLng);
			    				distance = Math.round(distance*10.0)/10.0;
			    				double miles = Math.round(distance*10.0*0.6214)/10.0;
								
								// this reference won't display in ListView - it will be hidden
								// the Places reference is used to get full details for each location
								map.put(KEY_REFERENCE, p.reference);
								
								// Place name
								map.put(KEY_NAME, p.name);
								
								map.put(KEY_DISTANCE, distance + " km (" + miles + " mi)");
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							SimpleAdapter adapter = new SimpleAdapter(PlacesActivity.this, placesListItems,
					                R.layout.list_item, new String[] { KEY_REFERENCE, KEY_NAME, KEY_DISTANCE }, new int[] {
					                        R.id.reference, R.id.name, R.id.distanceCount });
							
							// adding data to ListView
							lv.setAdapter(adapter);
						}
					}
					
					// Google Places error conditions
					else if(status.equals("ZERO_RESULTS")) {
						alert.showAlertDialog(PlacesActivity.this, "No results",
								"No places were found.", false);
					}
					else if(status.equals("UNKNOWN_ERROR")) {
						alert.showAlertDialog(PlacesActivity.this, "Google Places error",
								"Unknown error occured.", false);
					}
					else if(status.equals("OVER_QUERY_LIMIT")) {
						alert.showAlertDialog(PlacesActivity.this, "Google Places error",
								"Query limit for Google Places has been reached", false);
					}
					else if(status.equals("REQUEST_DENIED")) {
						alert.showAlertDialog(PlacesActivity.this, "Google Places error",
								"Request denied.", false);
					}
					else if(status.equals("INVALID_REQUEST")) {
						alert.showAlertDialog(PlacesActivity.this, "Google Places error",
								"Invalid request.", false);
					}
					else {
						alert.showAlertDialog(PlacesActivity.this, "Google Places error",
								"Error occured.", false);
					}
				}
			});
		}
	}
}
