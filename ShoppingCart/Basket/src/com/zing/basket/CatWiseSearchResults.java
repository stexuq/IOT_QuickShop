package com.zing.basket;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.basket.util.Product;


public class CatWiseSearchResults extends Activity 
{
			
		Typeface type;
		ListView searchResults;
		SQLiteDatabase sqLite;
		int count=0;
		String subCatCode;
		
		//This arraylist will have data as pulled from server. This will keep cumulating.
		ArrayList<Product> catWiseProductResults = new ArrayList<Product>();
		
		//Based on the subcatcode, only filtered products will be moved here from productResults
		ArrayList<Product> catWiseFilteredProductResults = new ArrayList<Product>();
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_catwisesearch);
			
			//ActionBar actionBar = getActionBar();
			//actionBar.hide();
			
			subCatCode = getIntent().getExtras().getString("subcategory");
			
			Log.d("basket","intent subcatcode-"+subCatCode);
			
			type= Typeface.createFromAsset(this.getAssets(),"fonts/book.TTF");
				
			searchResults = (ListView) findViewById(R.id.listview_search);
			
			myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(subCatCode);
			
			//searchResults.setAdapter(new SearchResultsAdapter1(this,product_name));
			
		}
		
		class myAsyncTask extends AsyncTask<String, Void, String> 
		{
			JSONParser jParser;
			JSONArray productList;
			String url=new String();
			String textSearch;
			
		

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				productList=new JSONArray();
				jParser = new JSONParser();
			}

			@Override
			protected String doInBackground(String... sText) {
				
				url="http://lawgo.in/lawgo/productsbycat/user/1/searchcat/"+sText[0];
				getProductList(url);
				this.textSearch = sText[0];
				Log.d("basket","textSearch -"+textSearch);
				Log.d("basket",url);
				return "OK";
				
			}

			public void getProductList(String url)
			{
				
				Product tempProduct = new Product();
				String matchFound = "N";
				
				try {
					
					JSONObject json = jParser.getJSONFromUrl(url);
					
					productList = json.getJSONArray("ProductListByCat");
			
					Log.d("basket",""+productList.length()+"");
				
					for(int i=0;i<productList.length();i++)
					{
						tempProduct = new Product();
						
						JSONObject obj=productList.getJSONObject(i);
						
						tempProduct.setProductCode(obj.getString("ProductCode"));
						tempProduct.setProductName(obj.getString("ProductName"));
						tempProduct.setProductGrammage(obj.getString("ProductGrammage"));
						tempProduct.setProductBarcode(obj.getString("ProductBarcode"));
						tempProduct.setProductDivision(obj.getString("ProductCatCode"));
						tempProduct.setProductDepartment(obj.getString("ProductSubCode"));
						tempProduct.setProductMRP(obj.getString("ProductMRP"));
						tempProduct.setProductBBPrice(obj.getString("ProductBBPrice"));
						
						matchFound = "N";
						
						for (int j=0; j < catWiseProductResults.size();j++)
						{
							
							if (catWiseProductResults.get(j).getProductCode().equals(tempProduct.getProductCode()))
							{
								matchFound = "Y";
							
							}
						}
						
						if (matchFound == "N")
						{
							catWiseProductResults.add(tempProduct);
						}
						
					}
					
					//Log.d("basket",""+catWiseProductResults.size()+"");

				} catch (Exception e) {
					e.printStackTrace();
					//return ("Exception Caught");
				}
			}
			@Override
			protected void onPostExecute(String result) {

					super.onPostExecute(result);
					
					if(result.equalsIgnoreCase("Exception Caught"))
					{
						Toast.makeText(getApplicationContext(), "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();
						Intent intent=new Intent(getApplicationContext(),HomeScreen.class);
						//pd.dismiss();
						intent.putExtra("userId", 1);
						startActivity(intent);
						finish();
					}
					else
					{
					
						filterProductArray(textSearch);
						//Log.d("basket","searchText ola-"+textSearch;
						//refresh view
						//Log.d("basket","size - "+catWiseFilteredProductResults.size());
						searchResults.setAdapter(new CatWiseSearchResultsAdapter(getApplicationContext(),catWiseFilteredProductResults));
						//pd.dismiss();
					}
			}

		}
		
		//this filters products from productResults and copies to filteredProductResults. 
		public void filterProductArray(String newText) 
		{
			//Log.d("basket","inside filterProductArray");
			String pName;
			
			catWiseFilteredProductResults.clear();
			for (int i = 0; i < catWiseProductResults.size(); i++)
			{
				//Log.d("basket","i="+i);
				//Log.d("basket","new text -dd"+newText);
				pName = catWiseProductResults.get(i).getProductDepartment();
				if ( pName.equals(newText))
				{
					catWiseFilteredProductResults.add(catWiseProductResults.get(i));
					//Log.d("basket","filtered-"+filteredProductResults.get(filteredProductResults.size()-1));
				}
			}
			
		}
}

class CatWiseSearchResultsAdapter extends BaseAdapter
{
	 private LayoutInflater layoutInflater;
	 //private ArrayList listData=new ArrayList();
	 private ArrayList<Product> productDetails=new ArrayList<Product>();
	 int count;
	 Typeface type;
	 Context context;
	 
	public CatWiseSearchResultsAdapter(Context context, ArrayList<Product> product_details) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
		 //Toast.makeText(context, "Inside Custom List one", Toast.LENGTH_LONG).show();
		 //this.listData=listData;
		 this.productDetails=product_details;
		 this.count= product_details.size();
		 this.context = context;
		 type= Typeface.createFromAsset(context.getAssets(),"fonts/book.TTF");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return productDetails.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		 ViewHolder holder;
	     
		 if (convertView == null) 
	     {
	    	 convertView = layoutInflater.inflate(R.layout.listtwo_searchresults, null);
	         holder = new ViewHolder();
	         holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
	         holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
	         holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
	         holder.product_bb = (TextView) convertView.findViewById(R.id.product_bb);
	         holder.product_bbvalue = (TextView) convertView.findViewById(R.id.product_bbvalue);
			 //holder.product_savings = (TextView) convertView.findViewById(R.id.product_savings);
			 //holder.product_savingsvalue = (TextView) convertView.findViewById(R.id.product_savingsvalue);
			 //holder.qty = (TextView) convertView.findViewById(R.id.qty);
			 //holder.product_value = (TextView) convertView.findViewById(R.id.product_value);
	         holder.addToCart = (Button) convertView.findViewById(R.id.add_cart);
			 convertView.setTag(holder);
	     } 
		 else 
	     {
	            holder = (ViewHolder) convertView.getTag();
	     }
	 
	      
		 holder.product_name.setText(productDetails.get(position).getProductName());
		 holder.product_name.setTypeface(type);
		 
		 
		 holder.product_mrp.setTypeface(type);
		 
		 holder.product_mrpvalue.setText(productDetails.get(position).getProductMRP());
		 holder.product_mrpvalue.setTypeface(type);
		 
		 
		 holder.product_bb.setTypeface(type);
		 
		 holder.product_bbvalue.setText(productDetails.get(position).getProductBBPrice());
		 holder.product_bbvalue.setTypeface(type);
		 
		 holder.addToCart.setOnClickListener(new MyPersonalClickListener("button_addtocart",productDetails.get(position),context));
		 //holder.product_savings.setTypeface(type);
		 
		 
		 //holder.product_savingsvalue.setTypeface(type);
		 
		 
		 //holder.qty.setTypeface(type);
		 
		 //holder.product_value.setTypeface(type);
		 
	     return convertView;
	}
	
	 static class ViewHolder 
	 {	        
	        TextView product_name;
	        TextView product_mrp;
	        TextView product_mrpvalue;
	        TextView product_bb;
	        TextView product_bbvalue;
	        TextView product_savings;
	        TextView product_savingsvalue;
	        TextView qty;
	        TextView product_value;
	        Button addToCart;
	               
	 }
	 
	 public class MyPersonalClickListener implements OnClickListener
     {

      String button_name;
      Product prod_name;
      int tempQty;
      int tempValue;
      SQLiteDatabase sqLite;
      Context context;
      
      public MyPersonalClickListener(String button_name, Product prod_name, Context context) 
      {
           this.prod_name = prod_name;
           this.button_name = button_name;
           this.context = context;
      }

      @Override
      public void onClick(View v)
      {
          Log.d("basket","OnClick - "+button_name+"-"+prod_name);
    	  if (button_name == "button_addtocart")
          {
    		  sqLite=context.openOrCreateDatabase("basketbuddy", context.MODE_PRIVATE, null);
    		  
    		  //check if item is already in cart
    		  Cursor cc = sqLite.rawQuery("SELECT PRODUCT_QTY, PRODUCT_VALUE FROM CART WHERE PRODUCT_CODE ="+Integer.parseInt(prod_name.getProductCode()), null);
    		  
    		  if (cc.getCount()== 0)
    		  {
    			  Log.d("basket","entry not found in cart -"+prod_name.getProductName());
    			  //if not found then insert, else update qty and product value. If qty > 10, dont update
          		  sqLite.execSQL("INSERT INTO CART (PRODUCT_CODE, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_GRAMMAGE"+
            	  ", PRODUCT_MRP, PRODUCT_BBPRICE, PRODUCT_DIVISION, PRODUCT_DEPARTMENT,PRODUCT_QTY,PRODUCT_VALUE) VALUES("+
          		  prod_name.getProductCode()+",'"+ prod_name.getProductName()+ "','" +
            	  prod_name.getProductBarcode()+"','"+ prod_name.getProductGrammage()+"',"+
          		  Integer.parseInt(prod_name.getProductMRP())+","+ Integer.parseInt(prod_name.getProductBBPrice())+","+
            	  Integer.parseInt(prod_name.getProductDivision())+","+Integer.parseInt(prod_name.getProductDepartment())+
            	  ",1,"+ Integer.parseInt(prod_name.getProductBBPrice())+")");
          		Toast.makeText(context,"Item "+prod_name.getProductName()+" added to Cart", Toast.LENGTH_LONG).show();
    		  }
    		  else
    		  {
    			  
    			  Log.d("basket","entry found in cart -"+prod_name.getProductName());
    			  if(cc.moveToFirst())
    				{
    					do{
    						tempQty=cc.getInt(0);
    						tempValue = cc.getInt(1);
    					}while(cc.moveToNext());
    				}
    			  
    			  if (tempQty < 10)
    			  {
    				  sqLite.execSQL("UPDATE CART SET PRODUCT_QTY = "+ (tempQty+1)+",PRODUCT_VALUE = "+ 
    				(Integer.parseInt(prod_name.getProductBBPrice())+tempValue)+" WHERE PRODUCT_CODE ="+
    				prod_name.getProductCode());
    				  Toast.makeText(context,"Item "+prod_name.getProductName()+" added to Cart", Toast.LENGTH_LONG).show();
    			  }
    		  }

    		  sqLite.close();
          }
    	  
      }

     }
}