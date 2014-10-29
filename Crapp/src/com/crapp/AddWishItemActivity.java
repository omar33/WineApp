package com.crapp;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.WishItem;
import com.crapp.model.WishList;

public class AddWishItemActivity extends Activity {
	private EditText editName;
	private EditText editPrice;
	private EditText editQuantity;
	private WishList wishList;
	private WishItem wishItem;
	private Button deleteButton;
	private Button postButton;
	private Button shareButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.add_wish_item, null);
		editName = (EditText) contentView.findViewById(R.id.edit_name);
		editPrice = (EditText) contentView.findViewById(R.id.edit_price);
		editQuantity = (EditText) contentView.findViewById(R.id.edit_quantity);

		deleteButton = (Button) contentView.findViewById(R.id.button_delete);
		postButton=(Button) contentView.findViewById(R.id.button_post);
		shareButton=(Button) contentView.findViewById(R.id.button_share);
		Button btn = (Button) contentView.findViewById(R.id.button_save);
		setupSaveButton(btn);

		setContentView(contentView);

		setupWishList();
		setupWishItem();
	}

	private void setupWishList() {
		Bundle bundle = getIntent().getExtras();
		if (null!=bundle && bundle.containsKey(Constants.keyWishListId)) {
			int wishListId = bundle.getInt(Constants.keyWishListId);
			wishList = DatabaseManager.getInstance().getWishListWithId(wishListId);	
		}
	}

	private void setupWishItem() {
		Bundle bundle = getIntent().getExtras();
		if (null!=bundle && bundle.containsKey(Constants.keyWishItemId)) {
			int wishItemId = bundle.getInt(Constants.keyWishItemId);
			wishItem = DatabaseManager.getInstance().getWishItemWithId(wishItemId);
			editName.setText(wishItem.getName());
			editPrice.setText(wishItem.getPrice());
			editQuantity.setText(wishItem.getQuantity());
			deleteButton.setVisibility(View.VISIBLE);
			setupDeleteButton();
		} else {
			deleteButton.setVisibility(View.INVISIBLE);
		}
	}

	private void setupSaveButton(Button btn) {
		final Activity activity = this;
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = editName.getText().toString();
				String Price = editPrice.getText().toString();
				String Quantity = editQuantity.getText().toString();
				boolean isValid = notEmpty(name) && notEmpty(Price) && notEmpty(Quantity);
				if (isValid) {
					if (null==wishItem) {
						createNewWishItem(name,Price,Quantity);

						Log.d("after crea" , wishList.getName());
						List<WishItem> item6 = wishList.getItems();
						int c = item6.size();
						Log.d("size of",String.valueOf(c));
						List<WishItem> item2 = DatabaseManager.getInstance().getAllWishItems();
						int d = item2.size();
						Log.d("2size",String.valueOf(d));
					} else {
						updateWishItem(name,Price,Quantity);
					}
					finish();
				} else {
					new AlertDialog.Builder(activity)
					.setTitle("Error")
					.setMessage("All fields must be filled")
					.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();
				}
			}
		});
	}

	private void setupDeleteButton() {
		if (null!=deleteButton) {
			final Activity activity = this;
			deleteButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(activity)
					.setTitle("Warning")
					.setMessage("Are you sure you would like to delete wish '"+wishItem.getName()+"'?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							DatabaseManager.getInstance().deleteWishItem(wishItem);
							dialog.dismiss();
							activity.finish();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();
				}
			});
		}
	}

	protected void updateWishItem(String name, String price, String quantity) {
		wishItem.setName(name);
		wishItem.setPrice(price);
		wishItem.setQuantity(quantity);
		DatabaseManager.getInstance().updateWishItem(wishItem);
	}

	boolean notEmpty(String s) {
		return null!=s && s.length()>0;
	}

	private void createNewWishItem(String name,String price, String quantity) {
		if (null!=wishList) {
			WishItem item = new WishItem();
			item.setName(name);
			item.setPrice(price);
			item.setQuantity(quantity);
			item.setList(wishList);
			DatabaseManager.getInstance().addWishItem(item);
		}
	}
}