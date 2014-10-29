package com.crapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crapp.userdb.DatabaseManager;
import com.crapp.model.UserInfo;
 
public class Registration extends Activity
{   

	private Spinner spinner1;
	private Spinner day;
	private Spinner month;
	private Spinner year;
    EditText reg_username,reg_password,reg_confpassword, reg_securityanswer;
    Button btnCreateAccount;
    TextView loginScreen;
    
    // !! UserDatabase userDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
 
        reg_username=(EditText)findViewById(R.id.reg_fullname);
        reg_password=(EditText)findViewById(R.id.reg_password);
        reg_confpassword=(EditText)findViewById(R.id.reg_confpassword);
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        reg_securityanswer = (EditText)findViewById(R.id.security_answer);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        day = (Spinner) findViewById(R.id.day);
        month = (Spinner) findViewById(R.id.month);
        year = (Spinner) findViewById(R.id.year);
        
        
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        Log.d("date",formattedDate);
        String curyear = formattedDate.substring(6);
        Log.d("year",curyear);
        
        List<String> list = new ArrayList<String>();
        for( int i = 0; i < 70; i++)
        {
        	int a = Integer.parseInt(curyear) - i;
        	list.add(String.valueOf(a));
        }
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	year.setAdapter(dataAdapter);
        
        
        
        
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secretquestion_arrays,
                R.layout.login_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() 
        {
 
            public void onClick(View arg0) 
             {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
        
        btnCreateAccount=(Button)findViewById(R.id.btnRegister);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() 
        {

        	public void onClick(View v) 
        	{
            
        		String userName=reg_username.getText().toString();
        		String password=reg_password.getText().toString();
        		String confirmPassword=reg_confpassword.getText().toString();
                String securityAnswer=reg_securityanswer.getText().toString();
                String securityQuestion= String.valueOf(spinner1.getSelectedItem());
                String nday = String.valueOf(day.getSelectedItem());
                String nmonth = String.valueOf(month.getSelectedItem());
                String nyear = String.valueOf(year.getSelectedItem());
                
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());
                String curyear = formattedDate.substring(6);
                String curmonth = formattedDate.substring(3,5);
                String curday = formattedDate.substring(0,2);
                //check if a field is left vacant
                if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                else if( (Integer.parseInt(curyear)-21) < Integer.parseInt(nyear))
                {
                	Toast.makeText(getApplicationContext(), "You need to be 21 to use this app", Toast.LENGTH_LONG).show();
                	return;
                }
                //check if both password matches
                else if((Integer.parseInt(curyear)-21) == Integer.parseInt(nyear))
                {
                	if(Integer.parseInt(curmonth) < Integer.parseInt(nmonth))
                	{
                		Toast.makeText(getApplicationContext(), "You need to be 21 to use this app", Toast.LENGTH_LONG).show();
                    	return;
                	}
                	if(Integer.parseInt(curmonth) == Integer.parseInt(nmonth))
                	{
                		if(Integer.parseInt(curday) < Integer.parseInt(nday))
                		{
                			Toast.makeText(getApplicationContext(), "You need to be 21 to use this app", Toast.LENGTH_LONG).show();
                        	return;
                		}
                	}
                }
                else if( DatabaseManager.getInstance().getUserInfoWithId(userName) != null)
                {
                	Toast.makeText(getApplicationContext(), "Username already exist. \n Pick a new one", Toast.LENGTH_LONG).show();
                	return;
                }  
                else if(!password.equals(confirmPassword))
            	{   
                	Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                	return;
            	}
            	else
            	{
            		Log.d("before","before");
            		UserInfo userInfo = new UserInfo(userName,password,securityQuestion,securityAnswer,(nday+nmonth+nyear));
            		Log.d("after","after");
            		DatabaseManager.getInstance().addUserInfo(userInfo);
            		Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
            		Intent intentHomePage=new Intent(getApplicationContext(),HomePage.class);
            		intentHomePage.putExtra(Constants.userName,userName);
                    startActivity(intentHomePage);
            	}
            }
        });
    }
    
    
    @Override
    protected void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        //userDatabase.close();
    }
    
}
