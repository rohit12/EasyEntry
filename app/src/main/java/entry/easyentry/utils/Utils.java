package entry.easyentry.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        String strTime = sdf.format(calendar.getTime());
        return strTime;
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = sdf.format(calendar.getTime());
        return strDate;
    }

}

