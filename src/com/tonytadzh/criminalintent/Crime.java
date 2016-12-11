package com.tonytadzh.criminalintent;

import org.json.JSONObject;
import org.json.JSONException;
import java.util.UUID;
import java.util.Date;
import android.text.format.DateFormat;

public class Crime
{
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";

	private UUID id;
	private String title;
	private boolean solved;
	private Date date;
	
	public Crime()
	{
	//генерирование уникального идентификатора
		id = UUID.randomUUID();
		date = new Date();/* {
			@Override
			public String toString() {
				DateFormat df = new DateFormat();
				String formattedDate = (String) df.format("E, MMM dd, yyyy", this);
				return formattedDate;
			}
		}; */
	}
	
	public Crime(JSONObject json) throws JSONException {
		id = UUID.fromString(json.getString(JSON_ID));
		title = json.getString(JSON_TITLE);
		solved = json.getBoolean(JSON_SOLVED);
		date = new Date(json.getLong(JSON_DATE));
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean isSolved() {
		return solved;
	}
	
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, id.toString());
		json.put(JSON_TITLE, title);
		json.put(JSON_SOLVED, solved);
		json.put(JSON_DATE, date.getTime());
		return json;
	}
	
	@Override
	public String toString() {
		return title;
	}
}
