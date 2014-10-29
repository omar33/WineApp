package com.crapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

	public void showAlertDialog(Context context, String title, String message, boolean status) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle(title)
			.setMessage(message)
        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {
        			// User cancelled the dialog
        		}
        });
		
		builder.create().show();
	}
}
