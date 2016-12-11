package com.tonytadzh.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab
{
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crime.json";
	private CriminalIntentJSONSerializer serializer;

	private static CrimeLab crimeLab;
	private Context appContext;
	private ArrayList<Crime> crimes;
	
	private CrimeLab(Context context) {
		appContext = context;
		serializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
		try {
			crimes = serializer.loadCrimes();
		} catch (Exception e) {
			crimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
	/*      for (int i = 0; i < 100; i++) {
			Crime c = new Crime();
			c.setTitle("Crime#" + i);
			c.setSolved(i % 2 == 0);
			crimes.add(c);
		}	*/
	}
	
	public static CrimeLab get(Context c) {
		if (crimeLab == null) {
			crimeLab = new CrimeLab(c.getApplicationContext());
		}
		return crimeLab;
	}
	
	public void addCrime(Crime c) {
		crimes.add(c);	
	}
	
	public void deleteCrime(Crime c) {
		crimes.remove(c);	
	}
	
	public ArrayList<Crime> getCrimes() {
		return crimes;
	}
	
	public Crime getCrime(UUID id) {
		for (Crime c : crimes) {
			if (c.getId().equals(id))
				return c;
		}
		return null;
	}
	
	public boolean saveCrimes() {
		try {
			serializer.saveCrimes(crimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch(Exception e) {
			Log.e(TAG, "Error saving crimes: ", e);
			return false;
		}
	}
}
