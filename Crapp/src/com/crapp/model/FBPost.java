package com.crapp.model;

import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.app.ProgressDialog;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.crapp.AsyncFacebookRunner;
import com.crapp.BaseRequestListener;
import com.crapp.Facebook;
import com.crapp.SessionStore;
import com.crapp.R;

/**
 * This example shows how to post status to Facebook wall.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */
public class FBPost extends Activity{
	private Facebook mFacebook;
	private CheckBox mFacebookCb;
	private ProgressDialog mProgress;
	
	private Handler mRunOnUi = new Handler();
	
	private static final String APP_ID = "372058442898426";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post);
		
		final EditText reviewEdit = (EditText) findViewById(R.id.review);
		final EditText nameEdit = (EditText) findViewById(R.id.winename);
		mFacebookCb				  = (CheckBox) findViewById(R.id.cb_facebook);
		
		mProgress	= new ProgressDialog(this);
		
		mFacebook 	= new Facebook(APP_ID);
		
		SessionStore.restore(mFacebook, this);

		if (mFacebook.isSessionValid()) {
			mFacebookCb.setChecked(true);
				
			String name = SessionStore.getName(this);
			name		= (name.equals("")) ? "Unknown" : name;
				
			mFacebookCb.setText("  Facebook  (" + name + ")");
		}
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = nameEdit.getText().toString();
				String review = reviewEdit.getText().toString();
				
				if (review.equals("")) return;
			
				if (mFacebookCb.isChecked()) postToFacebook(name, review);
			}
		});
	}
	
	private void postToFacebook(String name, String review) {	
		mProgress.setMessage("Posting ...");
		mProgress.show();
		
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		Bundle params = new Bundle();
    		
		params.putString("message", "A review of:\n"+ name + "\n\n" + review);
		params.putString("name", "Eye on Wine");
		params.putString("caption", "Creative Applications");
		params.putString("link", "http://www.reddit.com");
		params.putString("description", "Don't use our application.");
		params.putString("picture", "http://i.imgur.com/Rf5R4gW.jpg");
		
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}

	private final class WallPostListener extends BaseRequestListener {
        public void onComplete(final String response) {
        	mRunOnUi.post(new Runnable() {
        		@Override
        		public void run() {
        			mProgress.cancel();
        			
        			Toast.makeText(FBPost.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
        		}
        	});
        }
    }
}