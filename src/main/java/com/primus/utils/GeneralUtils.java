package com.primus.utils;

import java.time.LocalDate;
import java.util.Date;

public class GeneralUtils {

    public static Integer getDayofMonth(Date date)
    {
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        return localDate.getDayOfMonth();
    }

    public static Integer getMonth(Date date)
    {
        LocalDate localDate =  new java.sql.Date(date.getTime()).toLocalDate();
        return localDate.getMonth().getValue();
    }

    public static Integer getYear(Date date)
    {
        LocalDate localDate =  new java.sql.Date(date.getTime()).toLocalDate();
        return localDate.getYear();
    }
}
