package com.crapp;

import java.util.List;

import com.crapp.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.UserInfo;
import com.crapp.model.Inventory;

public class Inventory_AddWine extends Activity {
	EditText txtName;
	EditText txtPrice;
	TextView txtWines;
	EditText txtQuantity;
	Spinner spinType;
	private Handler mHandler;
	UserInfo userInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.inventory_addwine);
		
		String username = getIntent().getExtras().getString(Constants.userName);
		userInfo = DatabaseManager.getInstance().getUserInfoWithId(username);
		txtName=(EditText)findViewById(R.id.txtName);
		txtPrice=(EditText)findViewById(R.id.txtPrice);
		txtWines=(TextView)findViewById(R.id.txtWines);
		txtQuantity=(EditText)findViewById(R.id.txtQuantity);
		spinType=(Spinner)findViewById(R.id.spinType);
		mHandler=new Handler();
		mHandler.post(mUpdate);
		Log.d("1","3");

	}
	private Runnable mUpdate= new Runnable(){
		public void run(){
			{
				String username = getIntent().getExtras().getString(Constants.userName);
				userInfo = DatabaseManager.getInstance().getUserInfoWithId(username);
				List<Inventory> item = userInfo.getInventoryItems();
				txtWines.setText("Number of wines: "+ item.size());
				mHandler.postDelayed(this,1000);
			}
		}};

		@Override
		public void onStart()
		{
			//try
			//{
				super.onStart();
				Log.d("we","wef");
				List<Inventory> item = userInfo.getInventoryItems();
				Log.d("wessf","sfafas");
				txtWines.setText("Number of wines: "+(item.size()));
		//	}
		//	catch(Exception ex)
		//	{
//Log.d("asa","afakjfhkashf");
	//			CatchError(ex.toString());
		//	}
		}


		public void btnAddEmp_Click(View view)
		{
			boolean ok=true;
			try
			{
				Spannable spn=txtPrice.getText();
				String name=txtName.getText().toString();
				Spannable span=txtQuantity.getText();
				double price=Double.valueOf(spn.toString());
				int quantity=Integer.valueOf(span.toString());
				String wineType= String.valueOf(spinType.getSelectedItem());


				String username = getIntent().getExtras().getString(Constants.userName);
				userInfo = DatabaseManager.getInstance().getUserInfoWithId(username);

				Inventory item = new Inventory();
				item.setWineName(name);
				item.setPrice(price);
				item.setQuantity(quantity);
				item.setWineType(wineType);
				item.setUser(userInfo);
				DatabaseManager.getInstance().addInventory(item);			

			}
			catch(Exception ex)
			{
				ok=false;
				Log.d("here","heresaf");
				CatchError(ex.toString());
			}
			finally
			{
				if(ok)
				{
					String username = getIntent().getExtras().getString(Constants.userName);
					userInfo = DatabaseManager.getInstance().getUserInfoWithId(username);
					List<Inventory> item = userInfo.getInventoryItems();
					Inventory_Alerts.ShowEmpAddedAlert(this);
					txtWines.setText("Number of Wines: "+(item.size()));
				}
			}
		}

		void CatchError(String Exception)
		{
			Dialog diag=new Dialog(this);
			diag.setTitle("Wine NOT Added");
			TextView txt=new TextView(this);
			txt.setText("At least one field is Vacant.");
			diag.setContentView(txt);
			diag.show();
		}

		void NotifyEmpAdded()
		{
			Dialog diag=new Dialog(this);
			diag.setTitle("Add new Wine");
			TextView txt=new TextView(this);
			txt.setText("Wine Added Successfully");
			diag.setContentView(txt);
			diag.show();
			try {
				diag.wait(1000);
			} catch (InterruptedException e) {
				CatchError(e.toString());
			}
			diag.notify();
			diag.dismiss();
		}
}


