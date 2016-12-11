package com.tonytadzh.criminalintent;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Build;
import android.content.Intent;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class CrimeListFragment extends ListFragment 
{
	private static final String TAG = "CrimeListFragment";
	private boolean subtitleVisible;
	private ArrayList<Crime> crimes;
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
		Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (subtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		registerForContextMenu(listView);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (subtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}	
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				Crime crime = new Crime();
				CrimeLab.get(getActivity()).addCrime(crime);
				Intent i = new Intent(getActivity(), CrimePagerActivity.class);
				i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
				startActivityForResult(i, 0);
				return true;
			case R.id.menu_item_show_subtitle:
				if (getActivity().getActionBar().getSubtitle() == null) {
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					subtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				} else {
					getActivity().getActionBar().setSubtitle(null);
					subtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(crime);
				adapter.notifyDataSetChanged();
				return true;
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
//	Crime c = (Crime) getListAdapter().getItem(position);
		Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
//	Log.d(TAG, c.getTitle() + " was clicked");

		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
	}
	
	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}	
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//Если мы не получили представление, заполняем его
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater()
					.inflate(R.layout.list_item_crime, null);	
			}	
			//Настройка представления для обьекта Crime
			Crime c = getItem(position);
			
			TextView titleTextView = 
				(TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dateTextView =
				(TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox =
				(CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			return convertView;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.crimes_title);
		crimes = CrimeLab.get(getActivity()).getCrimes();
		
	//	ArrayAdapter<Crime> adapter = 
	//		new ArrayAdapter<Crime>(getActivity(),
	//							android.R.layout.simple_list_item_1,
	//							crimes);
	
		CrimeAdapter adapter = new CrimeAdapter(crimes);
		setListAdapter(adapter);
		setRetainInstance(true);
		subtitleVisible = false;
	}			
} 
