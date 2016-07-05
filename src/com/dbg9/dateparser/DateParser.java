package com.dbg9.dateparser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateParser {
	private IDateDescription description; 
	
	public DateParser(){
		this(null);
	}
	
	public DateParser(IDateDescription description){
		IDateDescription des = null; 
		
		if(description == null){
			des = new EnglishDateDescription(); 
		}
		
		this.description = des; 
	}
	
	public Date parseDate(String strDate, boolean truncate) throws DateParserException {
		Date res = null;
		
		if(strDate != null && strDate.trim().length() > 0){
			String trimmed = strDate.trim();
			
			res = parseFormats(trimmed);
			
			if(res == null){
				res = parseImmediate(trimmed);
			}
			
			if(res == null){
				res = parsePlus(trimmed);
			}
			
			if(res == null){
				res = parseNext(trimmed);
			}
			
			if(res == null){
				res = parseWeekDay(trimmed);
			}
			
			if(res == null){
				throw new DateParserException(description.getErrorMessage() + trimmed);
			}
			
			if(truncate){
				res = truncateDate(res);
			}
		}
		
		return res;
	}

	private Date truncateDate(Date date) {
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
	}

	private Date parseWeekDay(String strDate) {
		Date res = null;
		
		if(isWeekDay(strDate)){
			int index = getWeekDayIndex(strDate); 
			
			Date date = new Date(); 
			
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			
			while(calendar.get(Calendar.DAY_OF_WEEK) != index){
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			res = calendar.getTime();
		}
		
		return res;
	}

	private boolean isWeekDay(String strDate) {
		return getWeekDayIndex(strDate) >= 1 && getWeekDayIndex(strDate) <= 7;
	}

	private int getWeekDayIndex(String strDate) {
		int res = -1;
		
		boolean found = false; 
		
		for(int index = 0; index < 7; index ++){
			String[] val = description.getWeekDay(index + 1);
			
			for(String vl : val){

				if(vl.equalsIgnoreCase(strDate)){
					res = index + 1; 
					
					found = true; 
					break;
				}
			}
			
			if(found){
				break;
			}
		}
		
		return res;
	}

	private Date parseNext(String strDate) {
		Date res = null;
		
		// the first field is either next or previous and 
		// the second must be some date related field
		String[] items = strDate.split(" ");
		if(items.length == 2){
			String first = items[0]; 
			String second = items[1];
			
			if(isNext(first) || isPrevious(first)){
				String val = "+1"; 
				
				if(isPrevious(first)){
					val = "-1";
				}
				
				if(isDateField(second)){
					String strField = val + " " + second; 
					
					res = parsePlus(strField);
				}
			}
		}
		
		return res;
	}

	private boolean isNext(String val) {
		return checkValueArray(val, description.getNext());
	}

	private boolean isPrevious(String val) {
		return checkValueArray(val, description.getPrevious());
	}

	/**
	 * for this we need two words, the first one starting with +and a number, and then a valid date field - year, month, day etc
	 */
	private Date parsePlus(String strDate) {
		Date res = null;
		
		String[] items = strDate.split(" ");
		
		if(items.length == 2){
			String first = items[0];
			String second = items[1];
			
			if(first.startsWith("+") || first.startsWith("-")){
				int val = 1; 
				
				if(first.startsWith("-")){
					val = -1;
				}
				
				Integer in = parseInteger(first.substring(1));
				
				if(in != null){
					// calculate shift
					in = in * val;
					
					if(isDateField(second)){
						int field = 0; 
						
						if(isDay(second)){
							field = Calendar.DAY_OF_MONTH; 
						}
						else if(isWeek(second)){
							field = Calendar.WEEK_OF_YEAR; 
						}
						else if(isMonth(second)){
							field = Calendar.MONTH; 
						}
						else if(isYear(second)){
							field = Calendar.YEAR;
						}
						
						res = getShiftedDate(in, field);
					}
				}
			}
		}
		
		return res;
	}

	private boolean isDay(String vl) {
		return checkValueArray(vl, description.getDay());
	}

	private boolean isWeek(String vl) {
		return checkValueArray(vl, description.getWeek());
	}

	private boolean isMonth(String vl) {
		return checkValueArray(vl, description.getMonth());
	}

	private boolean isYear(String vl) {
		return checkValueArray(vl, description.getYear());
	}

	private boolean isDateField(String str) {
		return isDay(str) || isWeek(str) || isMonth(str) || isYear(str);
	}

	private Integer parseInteger(String vl) {
		Integer res = null;
		
		try {
			res = new Integer(vl);
		}
		catch(Exception e){
			// no implementation
		}
		
		return res;
	}

	private Date parseImmediate(String strDate) {
		Date res = null;
		
		boolean yesterday = isYesterday(strDate);
		boolean tomorrow = isTomorrow(strDate);
		boolean today = isToday(strDate);
		
		if(yesterday || tomorrow || today){
			int value = 1;
			
			if(yesterday){
				value = -1;
			}
			else if(today){
				value = 0;
			}
			
			res = getShiftedDate(value, Calendar.DAY_OF_MONTH);
		}
		
		return res; 
	}

	private Date getShiftedDate(int value, int field) {
		Date res;
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(field, value);
		
		res = calendar.getTime();
		return res;
	}

	private boolean isToday(String strDate) {
		return checkValueArray(strDate, description.getToday());
	}

	private boolean checkValueArray(String strDate, String[] td) {
		int nr = td.length;
		
		boolean res = false;
		
		for(String t : td){
			if(t.equalsIgnoreCase(strDate)){
				res = true; 
				
				break;
			}
		}
		
		return res;
	}

	private boolean isTomorrow(String strDate) {
		return checkValueArray(strDate, description.getTomorrow());
	}

	private boolean isYesterday(String strDate) {
		return checkValueArray(strDate, description.getYesterday());
	}

	private Date parseFormats(String strDate) {
		Date res = null;
		String[] formats = description.getFormats();
		
		if(formats != null){
			int nr = formats.length;
			
			for(String format : formats){

				try {
					DateFormat fmt = new SimpleDateFormat(format);
					res = fmt.parse(strDate);
					
					// if the format does not have year, adjust the year
					if(res != null && !format.contains("yy")){
						// adjust year
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(new Date());
						
						int year = calendar.get(Calendar.YEAR);
						
						calendar.setTime(res);
						calendar.set(Calendar.YEAR, year);
						
						res = calendar.getTime();
					}
				}
				catch(Exception e){
					// no implementation
				}
				
				if(res != null){
					break; // if it is successful, we are done
				}
			}
		}
		
		return res;
	}
}
