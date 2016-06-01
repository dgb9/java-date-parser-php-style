package com.dbg9.dateparser;

public interface IDateDescription {
	String[] getFormats();
	
	String[] getTomorrow();
	String[] getYesterday();
	String[] getToday();
	
	String[] getDay();
	String[] getWeek();
	String[] getMonth();
	String[] getYear();
	
	String[] getNext();
	String[] getPrevious();
	
	String getErrorMessage();
	
	/**
	 * nr is one based number that shows week day 1 is Sunday
	 */
	String[] getWeekDay(int nr);
}
