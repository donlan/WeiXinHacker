package dong.lan.weixinhacker.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 时间转换工具
 */
public class TimeUtil {



    private TimeUtil(){}

    public static String getTime(long time,String patter){

        SimpleDateFormat sdf1 = new SimpleDateFormat(patter, Locale.CHINA);
        Date date1  = new Date(time);
        return sdf1.format(date1);
    }

}
