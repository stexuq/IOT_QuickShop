package com.zing.basket;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPageAdapter extends FragmentPagerAdapter {
	private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;
    
	public FragmentPageAdapter(FragmentManager fm, Context context) {
		super(fm);
		mFragmentManager = fm;
		mFragmentTags = new HashMap<Integer, String>();
		mContext = context;
		// TODO Auto-generated constructor stub
	}


	@Override
	public Fragment getItem(int arg0) {
	
		switch (arg0)
		{
		case 0:
				return new Search();
		case 1: 
				return new MyCart();
		case 2:
				return new QuickOrder();
		default:
				break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }
	
	public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }
	

}
