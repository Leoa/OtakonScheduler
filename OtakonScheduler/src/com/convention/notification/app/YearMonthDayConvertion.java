package com.convention.notification.app;

public class YearMonthDayConvertion {
	
	//convert string to int for year, month, and day
	
	int year;
	int month;
	int day;
	
	public int Year(String h){

		 year=Integer.parseInt(h);
		
		return year;
		
	 }
	
	public int Month(String h){

		 month=Integer.parseInt(h);
		
		return month;
		
	 }
	
	public int Day(String h){

		 day=Integer.parseInt(h);
		
		return day;
		
	 }
	
	
}
