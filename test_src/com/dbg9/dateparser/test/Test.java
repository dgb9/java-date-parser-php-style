package com.dbg9.dateparser.test;

import com.dbg9.dateparser.DateParser;

import java.util.Date;

public class Test {
    public static void main(String[] args){
        try {
            Test instance = new Test();
            instance.proceed(true);
            instance.proceed(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void proceed(boolean truncate) {

        print("trying to parse a series with the truncate parameter: " + truncate);
        // null parameter is the same as initializing with EnglishDateDescription parameter
        DateParser parser = new DateParser(null);

        process(parser, "jan 12, 2015", truncate);
        process(parser, "next day", truncate);
        process(parser, "+1 days", truncate);
        process(parser, "+2 days", truncate);
        process(parser, "tomorrow", truncate);
        process(parser, "tom", truncate);
        process(parser, "friday", truncate);
        process(parser, "today", truncate);
        process(parser, "yesterday", truncate);
        process(parser, "+1 year", truncate);
        process(parser, "next year", truncate);
        process(parser, "next days", truncate);
        process(parser, "previous days", truncate);
        process(parser, "this should be an error", truncate);
        print("");
        print("");

    }

    private void process(DateParser parser, String s, boolean truncate) {
        try {
            Date parsed = parser.parseDate(s, truncate);

            print("date for: " + s + ", is: " + parsed);
        }
        catch(Exception e){
            print("error: " + e.getMessage());
        }
    }

    private void print(Object message) {
        System.out.println(message);
    }

}
