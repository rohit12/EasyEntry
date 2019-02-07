package entry.easyentry.utils;

import android.widget.EditText;

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(calendar.getTime());
        return strDate;
    }

    public static boolean isEditTextEmpty(EditText editText){
        if (editText.getText().toString().trim().length()>0)
            return false;
        return true;
    }

}

