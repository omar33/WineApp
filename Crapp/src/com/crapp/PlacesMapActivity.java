package com.crapp;

import java.util.ArrayList;
import java.util.List;

import com.crapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PlacesMapActivity extends FragmentActivity implements LocationListener, OnInfoWindowClickListener {
	
	private DialogFragment newFragment;
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private PlacesList nearPlaces;
	private double latitude, longitude;
	private int toggle = 0;
	private static final String MAPS_URL = "https://maps.google.com/maps?f=d&daddr=";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);
        
        // getting reference to the SupportMapFragment of map_places.xml
    	SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
    	
    	// getting GoogleMap object from the fragment
    	googleMap = fm.getMap();
    	googleMap.setMyLocationEnabled(true);
    	googleMap.setOnInfoWindowClickListener(this);
    	
    	// getting LocationManager object from system services
    	locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    	
    	// get passed in objects from the intent
    	latitude = getIntent().getDoubleExtra("latitude", 0);
    	longitude = getIntent().getDoubleExtra("longitude", 0);
    	nearPlaces = (PlacesList)getIntent().getSerializableExtra("near_places");
    	
    	if(!haveInternet(getApplicationContext())) {
        	newFragment = new InternetDialog();
            newFragment.show(getSupportFragmentManager(), "network");
        } else {
        	Criteria criteria = new Criteria();
        	String provider = locationManager.getBestProvider(criteria, true);
        	
        	// request further location updates
        	locationManager.requestLocationUpdates(provider, 0, 0, (LocationListener)this);

        	LatLngBounds.Builder builder = new LatLngBounds.Builder();
        	
        	List<Marker> markers = new ArrayList<Marker>();
        	
    		// check for null
    		if(nearPlaces.results != null) {
    			// loop through all the places
    			for(Place place : nearPlaces.results) {
    				double locLat = place.geometry.location.lat; // latitude
    				double lngLat = place.geometry.location.lng; // longitude
    				double distance = distFrom(latitude, longitude, locLat, lngLat);
    				LatLng location = new LatLng(locLat, lngLat);
    				
    				// convert to miles
    				distance = Math.round(distance*10.0)/10.0;
    				double miles = Math.round(distance*10.0*0.6214)/10.0;
    				Marker temp = googleMap.addMarker(new MarkerOptions().position(location)
    						.title(place.name)
    						.snippet(distance + " km (" + miles + " mi)"));
    				markers.add(temp);
    			}
    		}
    		
    		// for every marker in the List, include in the Bounds object
    		for(Marker x : markers)
    			builder.include(x.getPosition());
    		
    		// build the bounds object
    		final LatLngBounds bounds = builder.build();
        	
    		// set camera to include all markers and current location in the view
        	googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

        	    @Override
        	    public void onCameraChange(CameraPosition position) {
        	        // move camera
        	        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
        	        // remove listener to prevent position reset on camera move.
        	        googleMap.setOnCameraChangeListener(null);
        	    }
        	});
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
    protected void onPause() {
        super.onPause();

        // if locationManager is getting updates, stop them before pausing
        if(haveInternet(getApplicationContext()))
        	locationManager.removeUpdates((LocationListener)this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        // resume grabbing location updates
        if(haveInternet(getApplicationContext())) {
        	String provider = locationManager.getBestProvider(new Criteria(), true);
        	locationManager.requestLocationUpdates(provider, 0, 0, (LocationListener)this);
        }
        // if no network connection, throw a dialog
        else {
        	if(newFragment != null)
        		newFragment.dismiss();
        	newFragment = new InternetDialog();
            newFragment.show(getSupportFragmentManager(), "network");
        }
    }

	@Override
	public void onInfoWindowClick(Marker marker) {
		String url = MAPS_URL;
		String temp = marker.getPosition().toString();
		temp = temp.substring(10, temp.length()-1);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url + temp));
		intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
		startActivity(intent);
	}
	
	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}