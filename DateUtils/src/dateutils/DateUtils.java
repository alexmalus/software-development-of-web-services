/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dateutils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Alex
 */
public class DateUtils {

    /**
     *
     * @param date will have the form YY-MM-DD
     * @param time will have the form HH:MM
     * @return an XMLGregorianCalendar with minutes, hours, days, months and year set
     */
    public static XMLGregorianCalendar convertDateTimeToGregCal(String date, String time) {
        final String DATE_FORMAT = "%sT%s:00.000+00:00";
        String date_time = String.format(DATE_FORMAT, date, time);
        DatatypeFactory data_fact;
        data_fact = null;
        try {
            data_fact = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }

    return data_fact.newXMLGregorianCalendar(date_time);
    }

    public static XMLGregorianCalendar convertDateToGregCal(int day, int month, int year) {
        final String DATE_FORMAT = "%s-%02d-%02d";
        String date = String.format(DATE_FORMAT, year, month, day);
        String time = "00:00";

        return convertDateTimeToGregCal(date, time);
    }
    
}
