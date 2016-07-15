package com.zing.basket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuickOrder extends Fragment {
	
	View myFragmentView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("basket","quickorder saved instance");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		myFragmentView = inflater.inflate(R.layout.fragment_quickorder, container, false);
			
		return myFragmentView;
	}
}
