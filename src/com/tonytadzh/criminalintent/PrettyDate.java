package com.tonytadzh.criminalintent;

import java.util.Date;
import android.text.format.DateFormat;

public class PrettyDate extends Date
{
	@Override
	public String toString() {
		DateFormat df = new DateFormat();
		String formattedDate = (String) df.format("E, MMM dd, yyyy", this);
		return formattedDate;
	}
}
