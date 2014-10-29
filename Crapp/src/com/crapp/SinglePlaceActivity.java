package com.crapp;

import com.crapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SinglePlaceActivity extends FragmentActivity implements OnInfoWindowClickListener {
	
	private AlertDialogManager alert = new AlertDialogManager();
	private DialogFragment newFragment;
	private GoogleMap googleMap;
	private GooglePlaces googlePlaces;
	private PlaceDetails placeDetails;
	private ProgressDialog pDialog;
	private UiSettings uiSettings;
	private double latitude, longitude, currLat, currLng;
	private int toggle = 0;
	public static String KEY_REFERENCE = "reference";
	private static final String MAPS_URL = "https://maps.google.com/maps?f=d&daddr=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);
		
		Intent i = getIntent();
		
		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);
		
		// Getting reference to the SupportMapFragment of map_places.xml
    	SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
    	
    	// Getting GoogleMap object from the fragment
    	googleMap = fm.getMap();
    	
    	googleMap.setOnInfoWindowClickListener(this);
    	uiSettings = googleMap.getUiSettings();
    	uiSettings.setMyLocationButtonEnabled(false);
    	uiSettings.setAllGesturesEnabled(false);
    	
    	currLat = getIntent().getDoubleExtra("latitude", 0);
    	currLng = getIntent().getDoubleExtra("longitude", 0);
		
    	if(!haveInternet(getApplicationContext())) {
        	newFragment = new InternetDialog();
            newFragment.show(getSupportFragmentManager(), "network");
        } else {
        	// calling Async background thread
        	new LoadSinglePlaceDetails().execute(reference);
        }
	}
	
	public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null || !info.isConnected())
            return false;
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable Internet while roaming, just return false
            return true;
        }
        return true;
    }
	
	public double distFrom(double lat1, double lng1, double lat2, double lng2) {
		float[] result=new float[1];
		Location.distanceBetween (lat1, lng1, lat2, lng2, result);
		return (double)result[0]/1000;
	}
	
	@Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        
        if(!haveInternet(getApplicationContext())) {
        	if(newFragment != null)
        		newFragment.dismiss();
        	newFragment = new InternetDialog();
            newFragment.show(getSupportFragmentManager(), "network");
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu item) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_menu, item);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.map_type:
	    	if(toggle == 0) {
	    		item.setTitle("Hybrid");
	    		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    		toggle = 1;
	    	} else if(toggle == 1) {
	    		item.setTitle("Terrain");
	    		googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	    		toggle = 2;
	    	} else {
	    		item.setTitle("Map");
	    		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    		toggle = 0;
	    	}
		
	    default:
	    	break;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		String temp = MAPS_URL;
		temp = temp + placeDetails.result.geometry.location.lat + "," + placeDetails.result.geometry.location.lng;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(temp));
		intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
		startActivity(intent);
	}
	
	/**
	 * Background Async Task to load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
		
		/**
		 * Before starting background thread, show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// getting profile JSON
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					if(placeDetails != null) {
						String status = placeDetails.status;
						
						// check place details status
						// check for all possible status
						if(status.equals("OK")) {
							if (placeDetails.result != null) {
								latitude = placeDetails.result.geometry.location.lat;
								longitude = placeDetails.result.geometry.location.lng;
								double distance = distFrom(latitude, longitude, currLat, currLng);
			    				distance = Math.round(distance*10.0)/10.0;
			    				
			    				double miles = Math.round(distance*10.0*0.6214)/10.0;
						    	
						    	Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
						    			.title(placeDetails.result.name).snippet("Tap for directions"));
						    	marker.showInfoWindow();
						    	
						    	LatLng latLng = new LatLng(latitude, longitude);
						    	
						    	int price = placeDetails.result.price_level;
								
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String rating = Double.toString(placeDetails.result.rating);
								String priceLevel;
								if(price == 0)
									priceLevel = "";
								else if(price == 1)
									priceLevel = "$";
								else if(price == 2)
									priceLevel = "$$";
								else if(price == 3)
									priceLevel = "$$$";
								else
									priceLevel = "$$$$";

						    	// Showing the current location in Google Map
						    	googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								final TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_rating = (TextView) findViewById(R.id.rating);
								TextView lbl_price = (TextView) findViewById(R.id.price);
								TextView lbl_distance = (TextView) findViewById(R.id.distance);
								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								rating = rating == "0.0" ? "Not rated" : rating;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(phone);
								lbl_phone.setTextColor(Color.BLUE);
								lbl_rating.setText(Html.fromHtml("<b>Rating:</b> " + rating));
								lbl_price.setText(priceLevel);
								lbl_distance.setText(distance + " km (" + miles + " mi)");
								
								lbl_phone.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent dialIntent = new Intent(Intent.ACTION_DIAL);
										dialIntent.setData(Uri.parse("tel:+"+lbl_phone.getText().toString().trim()));
										startActivity(dialIntent);
									}
								});
							}
						}
						else if(status.equals("ZERO_RESULTS"))
							alert.showAlertDialog(SinglePlaceActivity.this, "No results", "No places were found.", false);
						else if(status.equals("UNKNOWN_ERROR"))
							alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Unknown error occured.", false);
						else if(status.equals("OVER_QUERY_LIMIT"))
							alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Daily query limit for Google Places reached", false);
						else if(status.equals("REQUEST_DENIED"))
							alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Error occured. Request denied", false);
						else if(status.equals("INVALID_REQUEST"))
							alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Error occured. Invalid request", false);
						else
							alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Unable to load data.", false);
					} else
						alert.showAlertDialog(SinglePlaceActivity.this, "Places error", "Unable to load data.", false);	
				}
			});
		}
	}
}