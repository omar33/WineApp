package com.crapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RadiusPickerDialog extends DialogFragment {
	
	Dialog dialog;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final CharSequence[] items = {"5 kilometers (3.1 miles)", "10 kilometers (6.2 miles)", "25 kilometers (15.5 miles)", "50 kilometers (31 miles)"};
		
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int radius = ((PlacesActivity) getActivity()).getRadius();
        if(radius == 5000)
        	radius = 0;
        else if(radius == 10000)
        	radius = 1;
        else if(radius == 25000)
        	radius = 2;
        else
        	radius = 3;
        
        builder.setTitle("Search radius")
        .setSingleChoiceItems(items, radius, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(which) {
	            	case 0:
	            		((PlacesActivity) getActivity()).setRadius(5000);
	            		getDialog().dismiss();
	                	break;
	                case 1:
	                	((PlacesActivity) getActivity()).setRadius(10000);
	                	getDialog().dismiss();
	                	break;
	                case 2:
	                	((PlacesActivity) getActivity()).setRadius(25000);
	                	getDialog().dismiss();
	                	break;
	                case 3:
	                	((PlacesActivity) getActivity()).setRadius(50000);
	                	getDialog().dismiss();
	                	break;
	                default:
	                	getDialog().dismiss();
	                	break;
	            }
			}
        });
        
        return builder.create();
    }

}