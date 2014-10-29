package com.crapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class InternetDialog extends DialogFragment {
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setMessage("Enable network connection?")
               .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int which) {
            		   getDialog().dismiss();
            	   }
               })
               .setNegativeButton("Wi-Fi", new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int which) {
            		   // User selects wi-fi button
            		   getDialog().dismiss();
            		   startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            	   }
               })
               .setNeutralButton("Network", new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int which) {
            		   // User selects network button
            		   getDialog().dismiss();
            		   startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            	   }
               });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}