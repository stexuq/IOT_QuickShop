package com.zing.basket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class StartScreen extends Activity {
	
	SQLiteDatabase sqLite;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_screen);
		
		//initializing the action bar and hiding it. 
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		 
		//create a database if it doesnt exist, if it exists, it will open the database.
		
		sqLite=this.openOrCreateDatabase("basketbuddy", MODE_PRIVATE, null);
		
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS USER (USER_ID VARCHAR,NAME VARCHAR,EMAIL VARCHAR)");
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS CITY_LIST (ID INTEGER primary key autoincrement,CITY_NAME VARCHAR)" );
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS USER_PREF (ID INTEGER primary key autoincrement,CITY_NAME VARCHAR default 'NONE', VOICE_ON VARCHAR, NOTIF VARCHAR)" );
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS CHECKLIST (ID INTEGER primary key autoincrement, PRODUCT_NAME VARCHAR, PRODUCT_CHECKED VARCHAR)");
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT_MASTER (ID INTEGER primary key autoincrement, PRODUCT_CODE INTEGER, PRODUCT_DIVISION INTEGER, PRODUCT_DEPARTMENT INTEGER"+ 
		"PRODUCT_GF INTEGER, PRODUCT_F INTEGER, PRODUCT_SUBF INTEGER, PRODUCT_NAME VARCHAR, PRODUCT_BARCODE INTEGER, PRODUCT_GRAMMAGE VARCHAR, PRODUCT_MRP INTEGER, PRODUCT_BBPRICE INTEGER)");
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS CART (ID INTEGER primary key autoincrement, PRODUCT_CODE INTEGER, PRODUCT_DIVISION INTEGER, PRODUCT_DEPARTMENT INTEGER"+ 
		"PRODUCT_GF INTEGER, PRODUCT_F INTEGER, PRODUCT_SUBF INTEGER, PRODUCT_NAME VARCHAR, PRODUCT_BARCODE VARCHAR, PRODUCT_GRAMMAGE VARCHAR, PRODUCT_MRP INTEGER, PRODUCT_BBPRICE INTEGER, PRODUCT_QTY INTEGER, PRODUCT_VALUE INTEGER)");
		sqLite.execSQL("CREATE TABLE IF NOT EXISTS ORDERHISTORY (ID INTEGER primary key autoincrement, ORDER_NO VARCHAR, PRODUCT_CODE INTEGER, PRODUCT_DIVISION INTEGER, PRODUCT_DEPARTMENT INTEGER"+ 
				"PRODUCT_GF INTEGER, PRODUCT_F INTEGER, PRODUCT_SUBF INTEGER, PRODUCT_NAME VARCHAR, PRODUCT_BARCODE VARCHAR, PRODUCT_GRAMMAGE VARCHAR, PRODUCT_MRP INTEGER, PRODUCT_BBPRICE INTEGER, PRODUCT_QTY INTEGER, PRODUCT_VALUE INTEGER)");
		//call asynctask to fetch list of city to populate the dropdown. 
		
		updateList ul=new updateList();
		ul.execute();
		
	}
	
	class updateList extends AsyncTask<Void, Void, String>
	{
		
		JSONParser jParser;
		String url=new String();
		
		 @Override
		 protected void onPreExecute() {
			 super.onPreExecute();
			 url="http://lawgo.in/lawgo/city?format=json";
			 jParser = new JSONParser();
		 }
		 
		 
		@Override
		protected String doInBackground(Void... params) 
		{
			try
			{
				JSONObject json=jParser.getJSONFromUrl(url);
				Cursor sample;
				try 
				{
					JSONArray jar=json.getJSONArray("city");
					Log.d("arindam", ""+jar.length());
					
					for(int i=0;i<jar.length();i++)
					{
						
						JSONObject j=jar.getJSONObject(i);
						
						//check if the city is already there in citylist.
						sample=sqLite.rawQuery("SELECT CITY_NAME FROM CITY_LIST WHERE CITY_NAME='"+j.getString("cityname")+"'",null);
						
						if(sample.moveToFirst())
						{
							continue;
						}
						else
						{
							sqLite.execSQL("insert into CITY_LIST (CITY_NAME) VALUES ('"+j.getString("cityname")+"')");
							Log.d("arindam",j.getString("cityname"));
						}
						
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					return "perror";
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return "error";
			}
			
			return "success";
		}
		
		protected void onPostExecute(String result) 
		{	
			 super.onPostExecute(result);
			 if(result.equals("perror"))
			 {
				 moveTaskToBack(true);
				 Toast.makeText(getApplicationContext(), "Error!!! Oops..that was humiliating. Could you please try again", Toast.LENGTH_LONG).show();
			 }
			 else if(result.equals("error"))
			 {
				 moveTaskToBack(true);
				 Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
			 }
			 else
			 {
				 new Handler().postDelayed(new Runnable(){
					 @Override
					 public void run()
					 {
						  
						 Cursor c=sqLite.rawQuery("SELECT CITY_NAME FROM USER_PREF",null);	
						 Log.d("arindam" ,"c count"+c.getCount());
						 if (c.getCount()== 0)
							{
							 	sqLite.execSQL("INSERT INTO USER_PREF (CITY_NAME, VOICE_ON, NOTIF) VALUES('NONE','Y','Y')");
							}
						 
						 
						 Cursor d=sqLite.rawQuery("SELECT CITY_NAME FROM USER_PREF",null);
						 Log.d("arindam" ,"d count"+d.getCount());
						 
						 if(d.moveToFirst())
						 {	
							 Log.d("arindam" ,"d NONE"+d.getString(0));
							 if(d.getString(0).equals("NONE"))
						    {
						    	Intent intent=new Intent(StartScreen.this,CityScreen.class);
						    	startActivity(intent);
						    }
						    else
						    {
						    	Intent intent=new Intent(StartScreen.this,HomeScreen.class);
						    	startActivity(intent);
						    }
							finish();	
						 }
					 }
				 },1000);
		     }
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		sqLite.close();
	}

}
