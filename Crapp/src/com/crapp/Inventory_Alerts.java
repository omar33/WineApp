package com.crapp;


import com.crapp.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.crapp.model.Inventory;
import com.crapp.userdb.DatabaseManager;

public class Inventory_Alerts {
public static void ShowEmpAddedAlert(Context con)
{
	AlertDialog.Builder builder=new AlertDialog.Builder(con);
	builder.setTitle("Add new Wine");
	builder.setIcon(android.R.drawable.ic_dialog_info);
	Inventory_DialogListner listner=new Inventory_DialogListner();
	builder.setMessage("Wine Added successfully");
	builder.setPositiveButton("ok", listner);
	
	AlertDialog diag=builder.create();
	diag.show();
}

public static AlertDialog ShowEditDialog(final Context con, final Inventory wine)
{
	AlertDialog.Builder b=new AlertDialog.Builder(con);
	b.setTitle("Wine Details");
	LayoutInflater li=LayoutInflater.from(con);
	View v=li.inflate(R.layout.inventory_editdialog, null);
	
	b.setIcon(android.R.drawable.ic_input_get);
	
	b.setView(v);
	final TextView txtName=(TextView)v.findViewById(R.id.txtDelName);
	final TextView txtPrice=(TextView)v.findViewById(R.id.txtDelPrice);
	final TextView txtQuantity=(TextView)v.findViewById(R.id.txtDelQuantity);
	
	final Spinner spin=(Spinner)v.findViewById(R.id.spinDiagType);
	
	txtName.setText(wine.getWineName());
	txtPrice.setText(String.valueOf(wine.getPrice()));
	txtQuantity.setText(String.valueOf(wine.getQuantity()));
	b.setPositiveButton("Modify", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			wine.setWineName(txtName.getText().toString());
			wine.setPrice(Double.valueOf(txtPrice.getText().toString()));
			wine.setQuantity(Integer.valueOf(txtQuantity.getText().toString()));
			wine.setWineType(String.valueOf(spin.getSelectedItem()));
			
			try
			{
				DatabaseManager.getInstance().updateInventory(wine);
			
			}
			catch(Exception ex)
			{
				CatchError(con, ex.toString());
			}
		}
	});
	
	b.setNeutralButton("Delete", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			DatabaseManager.getInstance().deleteInventory(wine);
		}
	});
	b.setNegativeButton("Cancel", null);
	
	return b.create();
	//diag.show();
	
}

static public void CatchError(Context con, String Exception)
{
	Dialog diag=new Dialog(con);
	diag.setTitle("Error");
	TextView txt=new TextView(con);
	txt.setText(Exception);
	diag.setContentView(txt);
	diag.show();
}





}


