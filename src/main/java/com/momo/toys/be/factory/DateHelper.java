package com.momo.toys.be.factory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper{

    private DateHelper(){

    }

    public static Date startTimeStamp(Date fromDate){

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(fromDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        return startCalendar.getTime();
    }

    public static Date endTimeStamp(Date toDate){

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(toDate);
        endCalendar.set(Calendar.DAY_OF_YEAR, endCalendar.get(Calendar.DAY_OF_YEAR)+1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        return endCalendar.getTime();
    }
}
