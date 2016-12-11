package com.tonytadzh.criminalintent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import android.content.Context;

public class CriminalIntentJSONSerializer
{
	private Context context;
	private String fileName;
	
	public CriminalIntentJSONSerializer(Context c, String f) {
		context = c;
		fileName = f;
	}
	
	public ArrayList<Crime> loadCrimes()
		throws JSONException, IOException {
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			//Открытие и чтение файла в StringBuilder 
			InputStream is = context.openFileInput(fileName);
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			//разбор JSON с использованием JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			//построение массива обьектов Crime по данным JSONObject
			for (int i = 0; i < array.length(); i++) {
				crimes.add(new Crime(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			//происходит при "начале с нуля"; не обращайте внимания
		} finally {
			if (reader != null)
				reader.close();
		}
		return crimes;
	}
	
	public void saveCrimes(ArrayList<Crime> crimes) 
		throws JSONException, IOException {
		//построение массива в json
		JSONArray array = new JSONArray();
		for (Crime c : crimes) {
			array.put(c.toJSON());
		}
		//запись файла на диск
		Writer writer = null;
		try {
			OutputStream outputStream = 
				context.openFileOutput(fileName, context.MODE_PRIVATE);
			writer = new OutputStreamWriter(outputStream);
			writer.write(array.toString());
		} finally {
			if (writer != null) 
				writer.close();
		}
	}
}
