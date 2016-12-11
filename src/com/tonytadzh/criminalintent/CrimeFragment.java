package com.tonytadzh.criminalintent;

import java.util.UUID;
import java.util.Date;
import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.text.TextWatcher;
import android.text.Editable;

public class CrimeFragment extends Fragment
{
	public static final String EXTRA_CRIME_ID =
	"com.tonytadzh.criminalintent.crime_id";
	
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	private Crime crime;
	private EditText titleField;
	private Button dateButton;
	private CheckBox solvedCheckBox;
	
	private void updateDate() {
		dateButton.setText(crime.getDate().toString());
	}
	
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID crimeId = (UUID) getArguments()
			.getSerializable(EXTRA_CRIME_ID);
		crime = CrimeLab.get(getActivity()).getCrime(crimeId);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
		if(requestCode == REQUEST_DATE) {
			Date date = (Date) data
				.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			crime.setDate(date);
			updateDate();
		}
	}
	
	@TargetApi(11)
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		titleField = (EditText) v.findViewById(R.id.crime_title);
		titleField.setText(crime.getTitle());
		titleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				crime.setTitle(c.toString());
			}
			
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
			
			public void afterTextChanged(Editable c) {}
		});
		
		dateButton = (Button) v.findViewById(R.id.crime_date);
		updateDate();
		//dateButton.setEnabled(false);
		dateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = getActivity()
					.getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment
					.newInstance(crime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		solvedCheckBox.setChecked(crime.isSolved());
		solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChanged) {
				crime.setSolved(isChanged);	
			}
		});
		
		return v;
	}
}
