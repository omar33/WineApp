package com.crapp;

import com.crapp.SimpleGestureFilter.SimpleGestureListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.*; 
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.UserInfo;
 
public class Login extends Activity implements SimpleGestureListener {
	Button btnSignIn;
    
    TextView btnSignUp;
    EditText login_name, login_password;
    //UserDatabase userDatabase;
    ImageButton faceLog;
    UserInfo userInfo;
    private SimpleGestureFilter detector;
    
    Facebook facebook = new Facebook("372058442898426");;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
 
    	DatabaseManager.init(this);
        // create a instance of SQLite Database
       // !! userDatabase=new UserDatabase(this);
        //!! userDatabase=userDatabase.open();

        // swipe moton detector
        detector = new SimpleGestureFilter(this,this);
 
        // Get The Reference Of Buttons 
        login_name=(EditText)findViewById(R.id.login_name);
        login_password=(EditText)findViewById(R.id.login_password);
        btnSignIn=(Button)findViewById(R.id.btnLogin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v){
	        	// get The User name and Password
	            String userName=login_name.getText().toString();
	            String password=login_password.getText().toString();
	
	            //DatabaseManager base = DatabaseManager.getInstance();
	            userInfo = DatabaseManager.getInstance().getUserInfoWithId(userName);
	            if(userInfo == null)
	            {
	            	Toast.makeText(Login.this, "Username does not exit", Toast.LENGTH_LONG).show();
	            	return;
	            }
	            String storedPassword = userInfo.getPassword();
	            // check if the Stored password matches with  Password entered by user
	            if(password.equals(storedPassword)){ 
	            	Toast.makeText(Login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();

                  /// Create Intent for SignUpActivity  abd Start The Activity
                  Intent intentHomePage=new Intent(getApplicationContext(),HomePage.class);
                  intentHomePage.putExtra(Constants.userName, userInfo.getUsername());
                  startActivity(intentHomePage);

	            } else {
	            	Toast.makeText(Login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
	            }
	        }
        });
         
        
        // retrieve button
        btnSignUp=(TextView)findViewById(R.id.linkRegister);
        // Set OnClick Listener on SignUp button 
        btnSignUp.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            /// Create Intent for SignUpActivity and start the activity
            Intent intentSignUP=new Intent(getApplicationContext(),Registration.class);
            startActivity(intentSignUP);
            }
        });
        
        // retrieve button
        faceLog=(ImageButton)findViewById(R.id.ImageButton01);
        // Set OnClick Listener on Facebewk button
        faceLog.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
		        facebook.authorize(Login.this, new String[] { "publish_actions" }, new DialogListener() {
		        	public void onComplete(Bundle values) {}

		            public void onFacebookError(FacebookError error) {}

		            public void onError(DialogError e) {}

		            public void onCancel() {}
		        });
            }
        });
        
    }
    

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    facebook.authorizeCallback(requestCode, resultCode, data);
	    
	}
    
	// Method to handleClick Event of Sign In Button
	public void forgottenPassword(View V) {
		final Dialog dialog = new Dialog(Login.this);
	    dialog.setContentView(R.layout.forgot_pass);
	    dialog.setTitle("Forgotten Password Retrieval");
	 
	    // get the References of views
	    final  EditText name=(EditText)dialog.findViewById(R.id.forgot_username);
	    final  EditText answer=(EditText)dialog.findViewById(R.id.forgot_security_answer);
	    final  Spinner forgotSpinner = (Spinner)dialog.findViewById(R.id.forgot_spinner);
	
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secretquestion_arrays,
                R.layout.login_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forgotSpinner.setAdapter(adapter);
        
	    Button btnSignIn=(Button)dialog.findViewById(R.id.retrieve_password);
	             
	    // Set On ClickListener
	    btnSignIn.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	// get Security Question and Security Answer
		        String userName=name.getText().toString();
		        //Log.d("fogotpass: username",userName);
		        String securityQuestion= String.valueOf(forgotSpinner.getSelectedItem());
		        //Log.d("forgotpass: sec?",securityQuestion);
		        String securityAnswer=answer.getText().toString();
		 
		        // fetch the Password form database for respective user name
		        // !! String storedQuestion=userDatabase.getSecurityQuestion(userName);
		        // !! String storedAnswer=userDatabase.getSecurityAnswer(userName);
		        // !! String password=userDatabase.getSinlgeEntry(userName);
		        UserInfo userInfo = DatabaseManager.getInstance().getUserInfoWithId(userName);
		        if(userInfo == null)
		        {
		        	Toast.makeText(Login.this, "Username does not exit", Toast.LENGTH_LONG).show();
		        	return;
		        }
		        String storedQuestion = userInfo.getSecurityQuestion();
		        String storedAnswer = userInfo.getSecurityAnswer();
		        String password = userInfo.getPassword();
		        
		 
		        // check if the Stored password matches with  Password entered by user
		        if(securityQuestion.equals(storedQuestion) && securityAnswer.equals(storedAnswer)) { 
		        	Toast.makeText(Login.this, "QUICK HERE IS YOUR PASSWORD: " + password, Toast.LENGTH_LONG).show();
		            dialog.dismiss();
		        } else {
		            Toast.makeText(Login.this, "Security Question and Security Answer dont match", Toast.LENGTH_LONG).show();
		        }
		    }
	    });
	            dialog.show();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        //userDatabase.close();
    }


    public void onBackPressed ()
    {
    
    }
    
    //ADDED ON METHOES FOR SWIPE!!!!
    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
    	this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) 
    {
       String str = "";
       switch (direction) 
       {
          case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
          	break;
          case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
          	break;
          case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
          	break;
          case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
          	break;
      }
      
       Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
       
       if(str == "Swipe Right")
       {
           Intent intentSearchWine=new Intent(getApplicationContext(),WineList.class);
           startActivity(intentSearchWine);
       }
    }

    @Override
    public void onDoubleTap() 
    {
    	Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}
