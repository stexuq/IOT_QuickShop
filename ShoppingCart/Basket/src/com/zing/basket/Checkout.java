package com.zing.basket;


import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.basket.util.Product;

public class Checkout extends Activity {

	ArrayList<Product> cart_list = new ArrayList<Product>();
	SQLiteDatabase sqLite;
	int count;
	String address ="";
	String productList="";
	String userId="1";
	String orderNo="";
	String numberOfItems;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		
		getCartData();
		
		
		Typeface type= Typeface.createFromAsset(getAssets(),"fonts/book.TTF");
		
		TextView addressHeader = (TextView) findViewById(R.id.header);
		addressHeader.setTypeface(type);
		
		TextView paymentHeader = (TextView) findViewById(R.id.header1);
		paymentHeader.setTypeface(type);
		
		final EditText name = (EditText) findViewById(R.id.name);
		name.setTypeface(type);
		
		final EditText address1 = (EditText) findViewById(R.id.address1);
		address1.setTypeface(type);
		
		final EditText address2 = (EditText) findViewById(R.id.address2);
		address2.setTypeface(type);
		
		final EditText city = (EditText) findViewById(R.id.city);
		city.setTypeface(type);
		
		final EditText state = (EditText) findViewById(R.id.state);
		state.setTypeface(type);
		
		final EditText pin = (EditText) findViewById(R.id.pin);
		pin.setTypeface(type);
		
		final EditText phone = (EditText) findViewById(R.id.phone);
		phone.setTypeface(type);
		
		Button submit = (Button) findViewById(R.id.submit);
		submit.setTypeface(type);
		
		submit.setOnClickListener(new OnClickListener()
		{
		
			@Override
			public void onClick(View arg0) {
				
				int i=0;
				String prod;
				
				//do validations
				
				//if all validations are ok, then proceed. 
				// construct the address
				address = address.concat(":name:");
				address = address.concat(name.getText().toString());
				address = address.concat(":address1:");
				address = address.concat(address1.getText().toString());
				address = address.concat(":address2:");
				address = address.concat(address2.getText().toString());
				address = address.concat(":city:");
				address = address.concat(city.getText().toString());
				address = address.concat(":state:");
				address = address.concat(state.getText().toString());
				address = address.concat(":pin:");
				address = address.concat(pin.getText().toString());
				address = address.concat(":phone:");
				address = address.concat(phone.getText().toString());
				address = address.replace(" ", "%20");
				//Log.d("arindam",address);
				
				//construct the order summary
				numberOfItems = String.valueOf(cart_list.size());
				//Log.d("arindam",numberOfItems);
				// construct the product details
				
				for (i=0;i<Integer.parseInt(numberOfItems);i++)
				{
					prod=":code:";
					prod = prod.concat(cart_list.get(i).getProductCode());
					prod = prod.concat(":qty:");
					prod = prod.concat(cart_list.get(i).getProductQty());
					prod = prod.concat(":value:");
					prod = prod.concat(cart_list.get(i).getProductValue());
					
					productList = productList.concat(prod);
				}
				
				//call asynctask
				
				
				myAsyncTask m = new myAsyncTask();
				m.execute();
			}
		}		
						
	  );
		
		
	}
	
	
public void getCartData() {
		
		//Log.d("arindam","inside getCartData");
		Product tempCartItem = new Product();
		
		cart_list.clear();
		sqLite= openOrCreateDatabase("basketbuddy", this.MODE_PRIVATE, null);
		Cursor c=sqLite.rawQuery("SELECT * FROM CART",null);
		count=0;
		if(c.moveToFirst())
		{
			do{
				
				tempCartItem = new Product();
				tempCartItem.setProductCode(c.getString(c.getColumnIndex("PRODUCT_CODE")));
				tempCartItem.setProductName(c.getString(c.getColumnIndex("PRODUCT_NAME")));
				tempCartItem.setProductBarcode(c.getString(c.getColumnIndex("PRODUCT_BARCODE")));
				tempCartItem.setProductGrammage(c.getString(c.getColumnIndex("PRODUCT_GRAMMAGE")));
				tempCartItem.setProductDivision(c.getString(c.getColumnIndex("PRODUCT_DIVISION")));
				tempCartItem.setProductDepartment(c.getString(c.getColumnIndex("PRODUCT_DEPARTMENT")));
				tempCartItem.setProductBBPrice(c.getString(c.getColumnIndex("PRODUCT_BBPRICE")));
				tempCartItem.setProductMRP(c.getString(c.getColumnIndex("PRODUCT_MRP")));
				tempCartItem.setProductQty(c.getString(c.getColumnIndex("PRODUCT_QTY")));
				tempCartItem.setProductValue(c.getString(c.getColumnIndex("PRODUCT_VALUE")));
				//Log.d("arindam",tempCartItem.getProductName());
				cart_list.add(tempCartItem);
				count++;
			}while(c.moveToNext());
			
		}
		
		sqLite.close();
		
	}
	
public void deleteCartData() {
	

	sqLite= openOrCreateDatabase("basketbuddy", this.MODE_PRIVATE, null);
	//Log.d("arindam","inside deleteCartData");
	sqLite.execSQL("DELETE FROM CART");
		
	sqLite.close();
	
}

public void addOrderData()
{
	int i;
	//Log.d("arindam","inside addOrderData");
	sqLite= openOrCreateDatabase("basketbuddy", this.MODE_PRIVATE, null);
	for (i=0; i<cart_list.size();i++)
	{
		sqLite.execSQL("INSERT INTO ORDERHISTORY (ORDER_NO, PRODUCT_CODE, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_GRAMMAGE"+
	    ", PRODUCT_MRP, PRODUCT_BBPRICE, PRODUCT_DIVISION, PRODUCT_DEPARTMENT,PRODUCT_QTY,PRODUCT_VALUE) VALUES('"+
	    orderNo + "',"+ cart_list.get(i).getProductCode()+",'"+ cart_list.get(i).getProductName()+ "','" +
	    cart_list.get(i).getProductBarcode()+"','"+ cart_list.get(i).getProductGrammage()+"',"+
	    Integer.parseInt(cart_list.get(i).getProductMRP())+","+ Integer.parseInt(cart_list.get(i).getProductBBPrice())+","+
	    Integer.parseInt(cart_list.get(i).getProductDivision())+","+Integer.parseInt(cart_list.get(i).getProductDepartment())+
	    ","+cart_list.get(i).getProductQty()+","+ Integer.parseInt(cart_list.get(i).getProductValue())+")");
	}
	
	sqLite.close();
}

class myAsyncTask extends AsyncTask<Void, Void, String> {
	JSONParser jParser;
	String url = new String();
	ProgressDialog pd;
	String response = new String();
	SQLiteDatabase sqLite;
	String result;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		url = "http://lawgo.in/lawgo/order?userid="
				+ userId + "&address=" + address + "&itemcount="+numberOfItems +"&orderlist=" + productList;
		//Log.d("arindam","url"+ url);
		
		jParser = new JSONParser();
		pd = new ProgressDialog(Checkout.this);
		pd.setMessage("Please Wait");
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(Void... arg0) {
		
		
		try {
		JSONObject json = jParser.getJSONFromUrl(url);
		
			response = json.getString("Status");
			Log.i("arindam",response);
			
			if (response.equalsIgnoreCase("Success")) {
					
					
					orderNo = json.getString("OrderNo");
					Log.i("arindam",orderNo);
					deleteCartData();
					addOrderData();
					result = "success";
				}
		} catch (Exception e) 
		{
			result = "connection error";
		}
		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		if (result.equalsIgnoreCase("success")) 
		{
			pd.dismiss();
			Toast.makeText(getApplicationContext(),"Order No "+orderNo+" successfully created.", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
			startActivity(intent);
			finish();
			
		} else {
			pd.dismiss();
			Toast.makeText(getApplicationContext(),
					"Network not aviliable, please try later.", Toast.LENGTH_LONG).show();
			finish();
		}

	}

	
}

}
