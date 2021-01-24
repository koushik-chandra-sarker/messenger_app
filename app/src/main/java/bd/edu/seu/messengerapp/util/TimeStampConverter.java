package bd.edu.seu.messengerapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeStampConverter {
    static SimpleDateFormat time12Format = new SimpleDateFormat("hh:mm a");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
    public static String TimeStampToDateOrTime(Long timestamp){
        String time = time12Format.format(new Date(Long.parseLong(timestamp.toString())));
        String date = dateFormat.format(new Date(Long.parseLong(timestamp.toString())));
        String currentDate = dateFormat.format(Calendar.getInstance().getTime().getTime());
        if (currentDate.equals(date)){
            return time;
        }else return date;

    }



}
