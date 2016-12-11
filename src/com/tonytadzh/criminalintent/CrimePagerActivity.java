package com.tonytadzh.criminalintent;

import java.util.UUID;
import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity
{
	private ViewPager viewPager;
	private ArrayList<Crime> crimes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		setContentView(viewPager);
		
		crimes = CrimeLab.get(this).getCrimes();
		
		FragmentManager fm = getSupportFragmentManager();
		viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return crimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Crime crime = crimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		
		UUID crimeId = (UUID) getIntent()
			.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i < crimes.size(); i++) {
			if (crimes.get(i).getId().equals(crimeId)) {
				viewPager.setCurrentItem(i);
				setTitle(crimes.get(i).getTitle());
				break;
			}
		}
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) { }
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			public void onPageSelected(int pos) {
				Crime crime = crimes.get(pos);
				if (crime.getTitle() != null) 
					setTitle(crime.getTitle());
			}
		});
	}
}
