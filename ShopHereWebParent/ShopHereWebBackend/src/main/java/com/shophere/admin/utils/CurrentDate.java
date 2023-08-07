package com.shophere.admin.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CurrentDate {

	public static String getCurrentDate() {
	        // Get the current date
	        LocalDate currentDate = LocalDate.now();
	       
	        // Define the desired date format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMM dd, yyyy");
	       
	        // Format the current date
	        String formattedDate = currentDate.format(formatter);
	       
	       return formattedDate;
}
}