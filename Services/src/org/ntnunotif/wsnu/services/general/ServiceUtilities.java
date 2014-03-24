package org.ntnunotif.wsnu.services.general;

import org.oasis_open.docs.wsn.bw_2.UnacceptableTerminationTimeFault;

import javax.xml.bind.DatatypeConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tormod on 24.03.14.
 */
public class ServiceUtilities {

    /**
     * Get termination from a time-string
     * @param time
     * @return
     * @throws UnacceptableTerminationTimeFault
     */
    public static long interpretTerminationTime(String time) throws UnacceptableTerminationTimeFault{

        /* Try XsdDuration first */
        if(isXsdDuration(time)){
            return extractXsdDuration(time);
        }else if(isXsdDatetime(time)){
            try{
                return extractXsdDatetime(time);
            /* This should never happen*/
            }catch(IllegalArgumentException e){
                throw new UnacceptableTerminationTimeFault();
            }
        }else{
             /* Neither worked, send an unacceptableTerminationTimeFault*/
            throw new UnacceptableTerminationTimeFault();
        }

    }

    public static long extractXsdDuration(String time){

        Pattern years, months, days, hours, minutes, seconds;
        years = Pattern.compile("[0-9]+Y");
        months = Pattern.compile("[0-9]+M");
        days = Pattern.compile("[0-9]+D");
        hours = Pattern.compile("[0-9]+H");
        minutes = Pattern.compile("[0-9]+M");
        seconds = Pattern.compile("[0-9]+S");

        long currentTimeStamp = System.currentTimeMillis();

        String times[] = time.split("T");

        Matcher matcher = years.matcher(times[0]);
        if(matcher.find()){
            currentTimeStamp += 24*365*3600*1000*Long.parseLong(matcher.group().replace("Y", ""));
        }

        matcher = months.matcher(times[0]);
        if(matcher.find()){
            currentTimeStamp += 24*30.5*3600*1000*Long.parseLong(matcher.group().replace("M", ""));
        }

        matcher = days.matcher(times[0]);
        if(matcher.find()){
            currentTimeStamp += 24*30.5*3600*1000*Long.parseLong(matcher.group().replace("D", ""));
        }

        matcher = hours.matcher(times[1]);
        if(matcher.find()){
            currentTimeStamp += 3600*1000*Long.parseLong(matcher.group().replace("H", ""));
        }

        matcher = minutes.matcher(times[1]);
        if(matcher.find()){
            currentTimeStamp += 60*1000*Long.parseLong(matcher.group().replace("M", ""));
        }

        matcher = seconds.matcher(times[1]);
        if(matcher.find()){
            currentTimeStamp += 1000*Long.parseLong(matcher.group().replace("S", ""));
        }

        return currentTimeStamp;
    }

    public static long extractXsdDatetime(String string){
        return DatatypeConverter.parseDateTime(string).getTimeInMillis();
    }

    /**
     * Checks if a string is formatted in XsdDatetime. This function might return true on strings that are validly formatted,
     * but contains invalid months. E.g. 2014-13-11T36:00:00Z-25:00, which is an invalid date in three places (date, hour and subtracted hour).
     * @param time
     * @return
     */
    public static boolean isXsdDatetime(String time){
        return time.matches("[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-6][0-9]:[0-6][0-9](Z|-[0-2][0-9]:[0-6][0-9])?");
    }

    /**
     * Checks if a string is a Xs:duration string with a regular expression.
     * @param time
     * @return
     */
    public static boolean isXsdDuration(String time) {
        return time.matches("^(-P|P)((([0-9]+Y)?([0-9]+M)?([0-9]+D))?)?(?:(T([0-9]+H)?([0-9]+M)?([0-9]+S)?))?");
    }
}
