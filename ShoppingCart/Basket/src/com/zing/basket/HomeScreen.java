package com.zing.basket;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.zing.basket.util.Category;
import com.zing.basket.util.SubCategory;

public class HomeScreen extends FragmentActivity implements ActionBar.TabListener 
{
	
ActionBar bar;
ViewPager viewpager;
FragmentPageAdapter ft;
Fragment mSearchFragment;
Fragment mCartFragment;
Fragment mQuickOrderFragment;

Search search;
MyCart mycart;
QuickOrder quickorder;

// here we define the widgets required for implementing the drawer layout. 
private DrawerLayout mDrawerLayout;
private ActionBarDrawerToggle mDrawerToggle;
private ExpandableListView mCategoryList;

// these are the arraylists for the categories and sub categories
private ArrayList<Category> category_name = new ArrayList<Category>();
private ArrayList<ArrayList<SubCategory>> subcategory_name = new ArrayList<ArrayList<SubCategory>>();
private ArrayList<Integer> subCatCount = new ArrayList<Integer>();

int previousGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		//populate the arraylists
		this.getCatData();		
		
		viewpager = (ViewPager) findViewById(R.id.pager);
        
        ft = new FragmentPageAdapter(getSupportFragmentManager(),this.getApplicationContext());
        viewpager.setAdapter(ft);
        
		final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        
        bar.addTab(bar.newTab().setText("Search").setTabListener(this));
        bar.addTab(bar.newTab().setText("Cart").setTabListener(this));
        bar.addTab(bar.newTab().setText("Quick Order").setTabListener(this));
        
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				bar.setSelectedNavigationItem(arg0);
				
				Fragment fragment = ((FragmentPageAdapter)viewpager.getAdapter()).getFragment(arg0);
				
				if (arg0 ==1 && fragment != null)
				{
					fragment.onResume();	
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        //new code for drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategoryList = (ExpandableListView) findViewById(R.id.left_drawer);
        
        //set up the adapter for the expandablelistview to display the categories. 
        
        mCategoryList.setAdapter(new expandableListViewAdapter(HomeScreen.this,category_name,subcategory_name, subCatCount));
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // enable ActionBar application icon to be used to open or close the drawer layout
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    
        //defining the behavior when any group is clicked in expandable listview
        mCategoryList.setOnGroupClickListener(new OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View view,
					int groupPosition, long id) {
				
				
		        if (parent.isGroupExpanded(groupPosition)) 
		        {
		            parent.collapseGroup(groupPosition);
		        } else 
		        {
		            if (groupPosition != previousGroup)
		            {
		            	parent.collapseGroup(previousGroup);
		            }
		            previousGroup = groupPosition;
		            parent.expandGroup(groupPosition);
		        }
		        
		        parent.smoothScrollToPosition(groupPosition);
				return true;
			}
			
		});
		
      //defining the behavior when any child is clicked in expandable listview 
		mCategoryList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				//calling CatWiseSearchResults with parameters of subcat code. 
				//CatWiseSearchResults will fetch items based on subcatcode. 
				
    			Intent intent=new Intent(HomeScreen.this,CatWiseSearchResults.class);
    			
    			ArrayList<SubCategory> tempList = new ArrayList<SubCategory>();
                tempList =  subcategory_name.get(groupPosition);
                
    			intent.putExtra("subcategory", tempList.get(childPosition).getSubCatCode());
    			startActivity(intent);
				mDrawerLayout.closeDrawer(mCategoryList);
				
    			return true;
			}
		});
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
		
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, 
                R.string.drawer_open, R.string.drawer_close ){
        	
                    @Override
                    public void onDrawerClosed(View view) {
                        
                        invalidateOptionsMenu();
                               
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        
                        invalidateOptionsMenu();
                        
                    }

                };
                
        mDrawerLayout.setDrawerListener(mDrawerToggle);
                
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }
	
	
	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewpager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
	        return true;
	    }
	    // Handle action buttons
	    return true;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    // Sync the toggle state after onRestoreInstanceState has occurred.
	    mDrawerToggle.syncState();
	}
	
	public class expandableListViewAdapter extends BaseExpandableListAdapter 
	{
		 
		private LayoutInflater layoutInflater;
		private ArrayList<Category> categoryName=new ArrayList<Category>();
		ArrayList<ArrayList<SubCategory>> subCategoryName = new ArrayList<ArrayList<SubCategory>>();
        ArrayList<Integer> subCategoryCount = new ArrayList<Integer>();
		int count;
		Typeface type;
        
        SubCategory singleChild = new SubCategory();
        
        public expandableListViewAdapter(Context context, ArrayList<Category> categoryName, ArrayList<ArrayList<SubCategory>> subCategoryName, ArrayList<Integer> subCategoryCount) 
        {
 
    		layoutInflater = LayoutInflater.from(context);
    		this.categoryName= categoryName;
    		this.subCategoryName = subCategoryName;
    		this.subCategoryCount = subCategoryCount;
    		this.count= categoryName.size();
    		
    		type= Typeface.createFromAsset(context.getAssets(),"fonts/book.TTF");
    		
    	}
        
        @Override
        public void onGroupCollapsed(int groupPosition) 
        {
         super.onGroupCollapsed(groupPosition);
        }
        
        @Override
        public void onGroupExpanded(int groupPosition) 
        {
         super.onGroupExpanded(groupPosition);
        }
        
        @Override
        public int getGroupCount() 
        {
        
            return categoryName.size();
        }
 
        @Override
        public int getChildrenCount(int i) 
        {
                	
        	return (subCategoryCount.get(i));
        	
        }
 
        @Override
        public Object getGroup(int i) 
        {
            return categoryName.get(i).getCatName();
        }
 
        @Override
        public SubCategory getChild(int i, int i1) 
        {
        	
        	ArrayList<SubCategory> tempList = new ArrayList<SubCategory>();
            tempList =  subCategoryName.get(i);
            return tempList.get(i1);
        	
        }
 
        @Override
        public long getGroupId(int i) {
            return i;
        }
 
        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }
 
        @Override
        public boolean hasStableIds() {
            return true;
        }
 
        @Override
        public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) 
        {
            
        	if (view == null)
        	{
        		view = layoutInflater.inflate(R.layout.expandablelistcategory, viewGroup, false);
        	}
        	
        	TextView textView = (TextView) view.findViewById(R.id.cat_desc_1);
            textView.setText(getGroup(i).toString());
            textView.setTypeface(type);
            
            return view;
            
        }
        
               
        @Override
        public View getChildView(int i, int i1, boolean isExpanded, View view, ViewGroup viewGroup) 
        {	
        	if (view == null)
        	{
        	view = layoutInflater.inflate(R.layout.expandablelistviewsubcat, viewGroup, false);
        	
        	}
        	
        	singleChild = getChild(i,i1);
        	
        	TextView childSubCategoryName = (TextView) view.findViewById(R.id.subcat_name);
        	childSubCategoryName.setTypeface(type);
        	
        	childSubCategoryName.setText(singleChild.getSubCatName());
        	
            return view;
            
        }
 
        @Override
        public boolean isChildSelectable(int i, int i1) 
        {
            return true;
        }
        
        @Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }
                      
    }
	
	public void getCatData()
	{
		category_name.clear();
		Category categoryDetails = new Category();
		
		categoryDetails.setCatCode(10);
		categoryDetails.setCatName("Grocery & Staples");
		
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(20);
		categoryDetails.setCatName("Biscuits, Snacks and Namkeens");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(30);
		categoryDetails.setCatName("Beverages");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(40);
		categoryDetails.setCatName("Packed Food and Condiments");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(50);
		categoryDetails.setCatName("Personal Care");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(60);
		categoryDetails.setCatName("Baby & Kids");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(70);
		categoryDetails.setCatName("Household Cleaning");
		category_name.add(categoryDetails);
		
		categoryDetails = new Category();
		categoryDetails.setCatCode(80);
		categoryDetails.setCatName("Metal, Plastics and Microwaveware");
		category_name.add(categoryDetails);
		
		//----Populate Sub Category Codes
		subcategory_name.clear();
		   
		ArrayList<SubCategory> subCategoryMatches = new ArrayList<SubCategory>();
		   
		SubCategory subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Dal & Pulses");
		subCategoryMatch.setSubCatCode("1001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Oil & Ghee");
		subCategoryMatch.setSubCatCode("1002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Flour");
		subCategoryMatch.setSubCatCode("1003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Spices, Seasoning, Cooking Pastes");
		subCategoryMatch.setSubCatCode("1004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Rice & Soya Products");
		subCategoryMatch.setSubCatCode("1005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Sugar & Salt");
		subCategoryMatch.setSubCatCode("1006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Organic");
		subCategoryMatch.setSubCatCode("1007");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Dry Fruits & Nuts");
		subCategoryMatch.setSubCatCode("1008");
		subCategoryMatches.add(subCategoryMatch);
		
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    //---
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Biscuits");
		subCategoryMatch.setSubCatCode("2001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Chips");
		subCategoryMatch.setSubCatCode("2002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Namkeen");
		subCategoryMatch.setSubCatCode("2003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Cookies, Cakes & Rusk");
		subCategoryMatch.setSubCatCode("2004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Popcorn");
		subCategoryMatch.setSubCatCode("2005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Diet Snacks");
		subCategoryMatch.setSubCatCode("2006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Confectionery");
		subCategoryMatch.setSubCatCode("2007");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Sweets");
		subCategoryMatch.setSubCatCode("2008");
		subCategoryMatches.add(subCategoryMatch);
		
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //---
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Juices");
		subCategoryMatch.setSubCatCode("3001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Soft Drinks");
		subCategoryMatch.setSubCatCode("3002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Milk Mixes");
		subCategoryMatch.setSubCatCode("3003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Fruit Drinks");
		subCategoryMatch.setSubCatCode("3004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Tea & Coffee");
		subCategoryMatch.setSubCatCode("3005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Flavoured Drinks");
		subCategoryMatch.setSubCatCode("3006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Energy Drinks");
		subCategoryMatch.setSubCatCode("3007");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Water");
		subCategoryMatch.setSubCatCode("3008");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Milk Drinks");
		subCategoryMatch.setSubCatCode("3009");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Powder Drinks");
		subCategoryMatch.setSubCatCode("3010");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Syrups / Squash");
		subCategoryMatch.setSubCatCode("3011");
		subCategoryMatches.add(subCategoryMatch);
		
		
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //---
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Breakfast & Cereals");
		subCategoryMatch.setSubCatCode("4001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Ketchup & Sauce");
		subCategoryMatch.setSubCatCode("4002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Spreads & Dressing");
		subCategoryMatch.setSubCatCode("4003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Ready To Eat, Ready to Cook");
		subCategoryMatch.setSubCatCode("4004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Noodles & Soup");
		subCategoryMatch.setSubCatCode("4005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Pasta");
		subCategoryMatch.setSubCatCode("4006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Breads");
		subCategoryMatch.setSubCatCode("4007");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Pickles, Olives and Papad");
		subCategoryMatch.setSubCatCode("4008");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Baking and Dessert Ingredients");
		subCategoryMatch.setSubCatCode("4009");
		subCategoryMatches.add(subCategoryMatch);
		
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //---
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Soaps & Handwash");
		subCategoryMatch.setSubCatCode("5001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Body Care");
		subCategoryMatch.setSubCatCode("5002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Hair Care");
		subCategoryMatch.setSubCatCode("5003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Mens Grooming");
		subCategoryMatch.setSubCatCode("5004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Oral Care");
		subCategoryMatch.setSubCatCode("5005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Talk & Deodorant");
		subCategoryMatch.setSubCatCode("5006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Facial Care");
		subCategoryMatch.setSubCatCode("5007");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Feminine Hygiene");
		subCategoryMatch.setSubCatCode("5008");
		subCategoryMatches.add(subCategoryMatch);
			
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //---baby and kids
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Baby Food");
		subCategoryMatch.setSubCatCode("6001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Baby Care");
		subCategoryMatch.setSubCatCode("6002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Diapers");
		subCategoryMatch.setSubCatCode("6003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Kids");
		subCategoryMatch.setSubCatCode("6004");
		subCategoryMatches.add(subCategoryMatch);
			
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //--- cleaning
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Fabric");
		subCategoryMatch.setSubCatCode("7001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Utensil");
		subCategoryMatch.setSubCatCode("7002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Household");
		subCategoryMatch.setSubCatCode("7003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Toilet");
		subCategoryMatch.setSubCatCode("7004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Shoes");
		subCategoryMatch.setSubCatCode("7005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Tissue Rolls");
		subCategoryMatch.setSubCatCode("7006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Brooms & Brushes");
		subCategoryMatch.setSubCatCode("7007");
		subCategoryMatches.add(subCategoryMatch);
		
		subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	    //---household goods
	    
	    subCategoryMatches = new ArrayList<SubCategory>();
		   
		subCategoryMatch = new SubCategory();
		   
		subCategoryMatch.setSubCatName("Freshners");
		subCategoryMatch.setSubCatCode("8001");
		subCategoryMatches.add(subCategoryMatch);
		   
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Repellents");
		subCategoryMatch.setSubCatCode("8002");
		subCategoryMatches.add(subCategoryMatch);
		    
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Pooja Needs");
		subCategoryMatch.setSubCatCode("8003");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Bulbs and CFLs");
		subCategoryMatch.setSubCatCode("8004");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("OTC Medicines");
		subCategoryMatch.setSubCatCode("8005");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Batteries");
		subCategoryMatch.setSubCatCode("8006");
		subCategoryMatches.add(subCategoryMatch);
		
		subCategoryMatch = new SubCategory();
		subCategoryMatch.setSubCatName("Disposibles & Napkins");
		subCategoryMatch.setSubCatCode("8007");
		subCategoryMatches.add(subCategoryMatch);
			
	    subcategory_name.add(subCategoryMatches);
	    subCatCount.add(subCategoryMatches.size());
	    
	}
}