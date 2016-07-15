package com.zing.basket;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zing.basket.util.Product;


@SuppressLint("ShowToast")
public class MyCart extends Fragment {
	
	
	ArrayList<Product> cart_list = new ArrayList<Product>();
	SQLiteDatabase sqLite;
	int count=0;
	int totalCartItemCount =0;
	int totalCartValue = 0;
	View myFragmentView;
	ListView lv1;
	final String[] qtyValues = {"1","2","3","4","5","6","7","8","9","10"};
	HomeScreen activity;
	TextView itemText;
	TextView itemCount;
	TextView shippingText;
	TextView shippingAmount;
	TextView totalAmount;
	Button checkout;
	TextView cartEmpty;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {
	  
		
		myFragmentView = inflater.inflate(R.layout.fragment_mycart, container, false);
	  
	  getCartData();
	  totalCartItemCount = cart_list.size();
	  totalCartValue =0;
	  
	  for (int temp1=0; temp1 < cart_list.size(); temp1++)
	  {
		  totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
	  }
	  
	  HomeScreen activity = (HomeScreen) getActivity();
	  
	  Typeface type= Typeface.createFromAsset(activity.getAssets(),"fonts/book.TTF");
	  
	  TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
	  TextView itemCount = (TextView) myFragmentView.findViewById(R.id.item_count);
	  TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
	  TextView shippingAmount = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
	  TextView totalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
	  Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
	  lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
	  TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);
	  
	  if (totalCartItemCount == 0)
	  {
		  itemText.setVisibility(myFragmentView.INVISIBLE);
		  itemCount.setVisibility(myFragmentView.INVISIBLE);
		  shippingText.setVisibility(myFragmentView.INVISIBLE);
		  shippingAmount.setVisibility(myFragmentView.INVISIBLE);
		  totalAmount.setVisibility(myFragmentView.INVISIBLE);
		  checkout.setVisibility(myFragmentView.INVISIBLE);
		  lv1.setVisibility(myFragmentView.INVISIBLE);
		  cartEmpty.setVisibility(myFragmentView.VISIBLE);
	  }
	  
	  else
	  {
		  itemText.setVisibility(myFragmentView.VISIBLE);
		  itemCount.setVisibility(myFragmentView.VISIBLE);
		  shippingText.setVisibility(myFragmentView.VISIBLE);
		  shippingAmount.setVisibility(myFragmentView.VISIBLE);
		  totalAmount.setVisibility(myFragmentView.VISIBLE);
		  checkout.setVisibility(myFragmentView.VISIBLE);
		  lv1.setVisibility(myFragmentView.VISIBLE);
		  cartEmpty.setVisibility(myFragmentView.INVISIBLE);
		  
	  } 
	  
	  
	  itemCount.setText("("+ totalCartItemCount + ")");
	  if (totalCartValue > 500)
	  {
		  shippingAmount.setText("Free");
		  totalAmount.setText("Rs "+ totalCartValue);
	  }
	  else
	  {
		  shippingAmount.setText("Rs 50");
		  totalAmount.setText("Rs "+ (totalCartValue+50));
	  }
	  
	  
	  itemText.setTypeface(type);
	  itemCount.setTypeface(type);
	  shippingText.setTypeface(type);
	  shippingAmount.setTypeface(type);
	  totalAmount.setTypeface(type);
	  checkout.setTypeface(type);
	  
	  
	 lv1.setAdapter(new custom_list_one(this.getActivity(),cart_list));
	  
	 checkout.setOnClickListener(new MyCheckoutClickListener("button_checkout"));
	  return myFragmentView;
	 }
	
	class custom_list_one extends BaseAdapter
	{
		 private LayoutInflater layoutInflater;
		 ViewHolder holder;
		 private ArrayList<Product> cartList=new ArrayList<Product>();
		 int cartCounter;
		 Typeface type;
		 Context context;
		 
		public custom_list_one(Context context, ArrayList<Product> cart_list) {
			layoutInflater = LayoutInflater.from(context);
			 this.cartList=cart_list;
			 this.cartCounter= cartList.size();
			 this.context = context;
			 type= Typeface.createFromAsset(context.getAssets(),"fonts/book.TTF");
		}

		@Override
		public int getCount() {

			return cartCounter;
		}

		@Override
		public Object getItem(int arg0) {

			return cartList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			Product tempProduct = cart_list.get(position);
			
			 if (convertView == null) 
		     {
		    	 convertView = layoutInflater.inflate(R.layout.listone_custom, null);
		         holder = new ViewHolder();
		         holder.name = (TextView) convertView.findViewById(R.id.product_name);
		         holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
		         holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
		         holder.qty = (Spinner) convertView.findViewById(R.id.spinner1);
		         holder.cancel = (ImageButton) convertView.findViewById(R.id.delete);
		         holder.product_value = (TextView) convertView.findViewById(R.id.product_value);
		         holder.qty_text =(TextView) convertView.findViewById(R.id.qty_text);
		         holder.product_bb = (TextView) convertView.findViewById(R.id.product_bb);
		         holder.product_bbvalue = (TextView) convertView.findViewById(R.id.product_bbvalue);
		         holder.product_savings = (TextView) convertView.findViewById(R.id.product_savings);
		         holder.product_savingsvalue = (TextView) convertView.findViewById(R.id.product_savingsvalue);
		         
		         convertView.setTag(holder);
		     } 
		     else 
		     {
		            holder = (ViewHolder) convertView.getTag();
		            
		     }
			 	
			 holder.name.setText(tempProduct.getProductName());
			 holder.name.setTypeface(type);
			 
			 holder.product_mrp.setTypeface(type);
			 
			 holder.product_mrpvalue.setText("Rs "+tempProduct.getProductMRP());
			 holder.product_mrpvalue.setTypeface(type);
			 
			 
			 ArrayAdapter<String> aa=new ArrayAdapter<String>(context,R.layout.qty_spinner_item,qtyValues);
			 aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
			 holder.qty.setAdapter(aa);
			
			 holder.qty.setSelection(Integer.parseInt(tempProduct.getProductQty())-1);
		
			 holder.product_bb.setTypeface(type);
			 
			 holder.product_bbvalue.setText("Rs "+tempProduct.getProductBBPrice());
			 holder.product_bbvalue.setTypeface(type);
			 
			 holder.product_savings.setTypeface(type);
			 
			 holder.product_savingsvalue.setText("Rs "+(Integer.parseInt(tempProduct.getProductMRP())-Integer.parseInt(tempProduct.getProductBBPrice()))*Integer.parseInt(tempProduct.getProductQty())+"");
			 holder.product_savingsvalue.setTypeface(type);
			 
			 holder.qty_text.setTypeface(type);
			 
			 holder.product_value.setText("Rs "+Integer.parseInt(tempProduct.getProductBBPrice())*Integer.parseInt(tempProduct.getProductQty())+"");
			 holder.product_value.setTypeface(type);
			 
			 holder.cancel.setOnClickListener(new MyPersonalClickListener("button_delete",tempProduct)); 
		     
			 holder.qty.setOnItemSelectedListener(new OnItemSelectedListener(){
			 
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,int selectionIndex, long id) 
				{
						//if user has changed the quantity, then save it in the DB. refresh cart_list
					
						if ((parent.getSelectedItemPosition()+1) != Integer.parseInt(cart_list.get(position).getProductQty()))
						{
							
							sqLite=context.openOrCreateDatabase("basketbuddy", context.MODE_PRIVATE, null);
							sqLite.execSQL("UPDATE CART SET PRODUCT_QTY ='"+ (parent.getSelectedItemPosition()+1)+"' WHERE PRODUCT_CODE ='"+cart_list.get(position).getProductCode()+"'");
							sqLite.execSQL("UPDATE CART SET PRODUCT_VALUE='" + (parent.getSelectedItemPosition()+1) * Integer.parseInt(cart_list.get(position).getProductBBPrice())  +"' WHERE PRODUCT_CODE ='"+cart_list.get(position).getProductCode()+"'");
							sqLite.close();
							getCartData();
							
							notifyDataSetChanged();
							
							//refresh data outside the listview - Cart Total, Total Items, Shipping Cost etc
							View parentView = (View) view.getParent().getParent().getParent().getParent();
							
							TextView txtTotalAmount = (TextView) parentView.findViewById(R.id.total_amount);
							TextView txtTotalItems = (TextView) parentView.findViewById(R.id.item_count);
							TextView txtShippingAmt = (TextView) parentView.findViewById(R.id.shipping_amount);
							
							totalCartItemCount = cart_list.size();
							totalCartValue =0;
							
							for (int temp1=0; temp1 < cart_list.size(); temp1++)
							{
								totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
							}
							
							txtTotalItems.setText("("+ totalCartItemCount + ")");
							
							  if (totalCartValue > 500)
							  {
								  txtShippingAmt.setText("Free");
								  txtTotalAmount.setText("Rs "+ totalCartValue);
							  }
							  else
							  {
								  txtShippingAmt.setText("Rs 50");
								  txtTotalAmount.setText("Rs "+ (totalCartValue+50));
							  }
							  
							  
						}
						
						
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) 
				{

				}
			 });
			 
		     return convertView;
		}
		 class ViewHolder 
		 {	        
		        TextView name;
		        TextView product_mrp;
		        TextView product_mrpvalue;
		        TextView product_bb;
		        TextView product_bbvalue;
		        TextView qty_text;
		        TextView product_savings;
		        TextView product_savingsvalue;
		        TextView product_value;
		        ImageButton cancel;
		        Spinner qty;
		        
		 }
			
	}
	
	public class MyPersonalClickListener implements OnClickListener
    {

      String button_name;
      Product prod_name;
      int tempQty;
      int tempValue;
      
      public MyPersonalClickListener(String button_name, Product prod_name) 
      {
           this.prod_name = prod_name;
           this.button_name = button_name;
      }

      @Override
      public void onClick(View v)
      {
  
    	  if (button_name == "button_delete")
          {
    		  sqLite=getActivity().openOrCreateDatabase("basketbuddy", getActivity().MODE_PRIVATE, null);
    		  sqLite.execSQL("DELETE FROM CART WHERE PRODUCT_CODE ="+Integer.parseInt(prod_name.getProductCode()));
      		  sqLite.close();
      		  Toast.makeText(getActivity(),"Item "+prod_name.getProductName()+" deleted from Cart", Toast.LENGTH_LONG).show();
      		  
      		  getCartData();
      		  
      		  View lView = (View) v.getParent().getParent();
      		  
      		  ((ListView) lView).setAdapter(new custom_list_one(getActivity(),cart_list));
      	
      		  
      		  TextView txtTotalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
      		  TextView txtTotalItems = (TextView) myFragmentView.findViewById(R.id.item_count);
      		  TextView txtShippingAmt = (TextView) myFragmentView.findViewById(R.id.shipping_amount);			
      		  TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
	      	  TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
	      	  Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
	      	  ListView lv1=(ListView) myFragmentView.findViewById(R.id.listView1);
	      	  TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);
      		  
      		  totalCartItemCount = cart_list.size();
			  totalCartValue =0;
			  for (int temp1=0; temp1 < cart_list.size(); temp1++)
			  {
				  totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
			  }
				
			  txtTotalItems.setText("("+ totalCartItemCount + ")");
				
			  if (totalCartValue > 500)
			  {
				  txtShippingAmt.setText("Free");
				  txtTotalAmount.setText("Rs "+ totalCartValue);
			  }
			  else
			  {
				  txtShippingAmt.setText("Rs 50");
				  txtTotalAmount.setText("Rs "+ (totalCartValue+50));
			  }
			  
			  
			  if (totalCartItemCount == 0)
			  {
				  itemText.setVisibility(myFragmentView.INVISIBLE);
				  txtTotalItems.setVisibility(myFragmentView.INVISIBLE);
				  shippingText.setVisibility(myFragmentView.INVISIBLE);
				  txtShippingAmt.setVisibility(myFragmentView.INVISIBLE);
				  txtTotalAmount.setVisibility(myFragmentView.INVISIBLE);
				  checkout.setVisibility(myFragmentView.INVISIBLE);
				  lv1.setVisibility(myFragmentView.INVISIBLE);
				  cartEmpty.setVisibility(myFragmentView.VISIBLE);
			  }
			  
			  else
			  {
				  itemText.setVisibility(myFragmentView.VISIBLE);
				  txtTotalItems.setVisibility(myFragmentView.VISIBLE);
				  shippingText.setVisibility(myFragmentView.VISIBLE);
				  txtShippingAmt.setVisibility(myFragmentView.VISIBLE);
				  txtTotalAmount.setVisibility(myFragmentView.VISIBLE);
				  checkout.setVisibility(myFragmentView.VISIBLE);
				  lv1.setVisibility(myFragmentView.VISIBLE);
				  cartEmpty.setVisibility(myFragmentView.INVISIBLE);
				  
			  } 

          }
    	  
      }

   }
	
	public class MyCheckoutClickListener implements OnClickListener
    {

      String button_name;
   
      public MyCheckoutClickListener(String button_name) 
      {
           this.button_name = button_name;
      }

      @Override
      public void onClick(View v)
      {
  
    	  if (button_name == "button_checkout")
          {
    		  
    		  Intent intent=new Intent(getActivity(),Checkout.class);
				startActivity(intent);
    		    //getActivity().finish();
          }
    	  
      }

   }
	
	public void getCartData() {
		

		HomeScreen activity = (HomeScreen) getActivity();
		Product tempCartItem = new Product();
		
		cart_list.clear();
		sqLite=activity.openOrCreateDatabase("basketbuddy", activity.MODE_PRIVATE, null);
		Cursor c=sqLite.rawQuery("SELECT  * FROM CART",null);
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
				cart_list.add(tempCartItem);
				count++;
			}while(c.moveToNext());
			
		}
		sqLite.close();
		
	}
	
	@Override
	public void onResume()
	{
		
		getCartData();
		
		TextView itemText = (TextView) myFragmentView.findViewById(R.id.item_text);
		TextView itemCount = (TextView) myFragmentView.findViewById(R.id.item_count);
		TextView shippingText = (TextView) myFragmentView.findViewById(R.id.shipping_text);
		TextView shippingAmount = (TextView) myFragmentView.findViewById(R.id.shipping_amount);
		TextView totalAmount = (TextView) myFragmentView.findViewById(R.id.total_amount);
		TextView cartEmpty = (TextView) myFragmentView.findViewById(R.id.cart_empty);
		Button checkout = (Button) myFragmentView.findViewById(R.id.checkout);
		
		totalCartItemCount = cart_list.size();
		  totalCartValue =0;
		  
		  for (int temp1=0; temp1 < cart_list.size(); temp1++)
		  {
			  totalCartValue = totalCartValue + Integer.parseInt(cart_list.get(temp1).getProductValue());
		  }
		
		  lv1.setAdapter(new custom_list_one(this.getActivity(),cart_list));
		  
		  itemCount.setText("("+ totalCartItemCount + ")");
		  if (totalCartValue > 500)
		  {
			  shippingAmount.setText("Free");
			  totalAmount.setText("Rs "+ totalCartValue);
		  }
		  else
		  {
			  shippingAmount.setText("Rs 50");
			  totalAmount.setText("Rs "+ (totalCartValue+50));
		  }
		  
		  if (totalCartItemCount == 0)
		  {
			  itemText.setVisibility(myFragmentView.INVISIBLE);
			  itemCount.setVisibility(myFragmentView.INVISIBLE);
			  shippingText.setVisibility(myFragmentView.INVISIBLE);
			  shippingAmount.setVisibility(myFragmentView.INVISIBLE);
			  totalAmount.setVisibility(myFragmentView.INVISIBLE);
			  checkout.setVisibility(myFragmentView.INVISIBLE);
			  lv1.setVisibility(myFragmentView.INVISIBLE);
			  cartEmpty.setVisibility(myFragmentView.VISIBLE);
		  }
		  
		  else
		  {
			  itemText.setVisibility(myFragmentView.VISIBLE);
			  itemCount.setVisibility(myFragmentView.VISIBLE);
			  shippingText.setVisibility(myFragmentView.VISIBLE);
			  shippingAmount.setVisibility(myFragmentView.VISIBLE);
			  totalAmount.setVisibility(myFragmentView.VISIBLE);
			  checkout.setVisibility(myFragmentView.VISIBLE);
			  lv1.setVisibility(myFragmentView.VISIBLE);
			  cartEmpty.setVisibility(myFragmentView.INVISIBLE);
			  
		  }  
		super.onResume();
		
		
	}
}