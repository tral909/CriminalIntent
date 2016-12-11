package com.tonytadzh.criminalintent;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;


public abstract class SingleFragmentActivity extends FragmentActivity
{
	protected abstract Fragment createFragment();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        
        if (fragment == null) {
        	fragment = createFragment();
        	fm.beginTransaction()
        		.add(R.id.fragmentContainer, fragment)
        		.commit();
        }
    }
}
