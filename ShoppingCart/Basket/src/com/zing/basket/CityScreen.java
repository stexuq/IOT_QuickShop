package com.zing.basket;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class CityScreen extends Activity {
	
	SQLiteDatabase sqLite;
	Spinner city_spinner;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_screen);
		
		Typeface type= Typeface.createFromAsset(getAssets(),"fonts/book.TTF");
		
		//This section is to hide the action bar. 
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		//Ideally SQL should be handled in a separate helper, but for ease of understanding to start off, I
		//have kept the code here. 
		sqLite=this.openOrCreateDatabase("basketbuddy", MODE_PRIVATE, null);
		
		Cursor c=sqLite.rawQuery("SELECT CITY_NAME FROM CITY_LIST",null);
		
		//ideally atleast 1 city should be there in city_name. As I have already synced this with the server
		// in StartScreen.java
		
		if (c.getCount()== 0)
		{
			Toast.makeText(getApplicationContext(), "Oh ho..Some unexpected problem. Please restart the application", Toast.LENGTH_LONG).show();
			
		}
		
		TextView city_selection=(TextView) findViewById(R.id.SelectCityText);
		city_selection.setTypeface(type);
		
		//defining the array that will hold the City Names. 
		String[] city_name_db=new String[(c.getCount()+1)];
				
				
		//by default, the first entry for city list is "Choose City". We will understand why 
		//this is necessary later.
		city_name_db[0] = "Choose City";
		
		//moving the city names from sqlite to an array city_name_db
		if(c.moveToFirst())
		{
			int count=1;
			do{
				city_name_db[count]=c.getString(0);
				count++;
			}while(c.moveToNext());
		}
		
		//creating an ArrayAdapter for the spinner and then associating the city ArrayAdapter
		//to the spinner.
		
		ArrayAdapter<String > aa=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,city_name_db);
		city_spinner = (Spinner) findViewById(R.id.spinner1);
		city_spinner.setAdapter(aa);
		
		//There is an inherent problem with Spinners. Lets assume that there are 3 cities Delhi, Gurgaon and Noida. The
		//moment I populate these 3 cities to the spinner, by default Delhi will get selected as this is the 
		//first entry. OnItemSelectedListener will get triggered immediately with Delhi as selection and the code 
		//will proceed. Net net, even the default first value is taken as an ItemSelected trigger. 
		
		//the way to bypass this is to add a default value 'Choose City' in the ArrayAdapter list. And then
		//inside the onItemSelected method, ignore if 'Choose City' has been selected. 
		
		//we are calling the SetOnItemSelectedListener, which will get called whenever any item is selected in
		//the spinner
		city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) 
			{
				if (parent.getItemAtPosition(position).equals("Choose City"))
				{
					//do nothing.
					
				}
				else
				{
					// save the selected city as a default city for shopping
					//this city selection is saved in DB. We may even decide to send this data to server, 
					//however in this example, we are not doing so. 
					
					String city_name=city_spinner.getSelectedItem().toString();
					Cursor c=sqLite.rawQuery("SELECT CITY_NAME FROM USER_PREF",null);
					if (c.getCount()== 0)
					{
						sqLite.execSQL("insert into USER_PREF (CITY_NAME, VOICE_ON) VALUES ('"+city_name+"','Y','Y')");
					}
					if(c.moveToFirst())
					{
						sqLite.execSQL("update USER_PREF set CITY_NAME = '"+city_name+"'");
					}
					
					Intent intent=new Intent(CityScreen.this,HomeScreen.class);
					startActivity(intent);
					sqLite.close();
					finish();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
			
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

}
