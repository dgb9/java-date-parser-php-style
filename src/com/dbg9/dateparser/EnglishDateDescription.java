package com.dbg9.dateparser;

public class EnglishDateDescription implements IDateDescription {

	@Override
	public String[] getFormats() {
		return new String[]{"MMMdd,yyyy", "MMMdd"};
	}

	@Override
	public String[] getTomorrow() {
		return new String[]{"tomorrow", "tom"};
	}

	@Override
	public String[] getYesterday() {
		return new String[]{"yesterday"};
	}

	@Override
	public String[] getWeek() {
		return new String[]{"weeks", "week"};
	}

	@Override
	public String[] getMonth() {
		return new String[]{"month", "months"};
	}

	@Override
	public String[] getYear() {
		return new String[] {"year", "years"};
	}

	@Override
	public String[] getNext() {
		return new String[]{"next"};
	}

	@Override
	public String[] getPrevious() {
		return new String[]{"previous"};
	}

	@Override
	public String getErrorMessage() {
		return "Cannot parse date: ";
	}

	@Override
	public String[] getToday() {
		return new String[]{"today", "tod"};
	}

	@Override
	public String[] getDay() {
		return new String[]{"day", "days"};
	}

	@Override
	public String[] getWeekDay(int nr) {
		String[] res = null;
		
		if(nr == 1){
			res = new String[]{"sun", "sunday"};
		}
		else if(nr == 2){
			res = new String[]{"mon", "monday"};
		}
		else if(nr == 3){
			res = new String[]{"tue", "tuesday"};
		}
		else if(nr == 4){
			res = new String[]{"wed", "wednesday"};
		}
		else if(nr == 5){
			res = new String[]{"thu", "thursday"};
		}
		else if(nr == 6){
			res = new String[]{"fri", "friday"};
		}
		else if(nr == 7){
			res = new String[]{"sat", "saturday"};
		}
		
		return res;
	}

}
