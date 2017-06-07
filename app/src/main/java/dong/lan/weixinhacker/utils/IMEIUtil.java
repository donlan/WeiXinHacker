package dong.lan.weixinhacker.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取IMEI
 */

public class IMEIUtil {

    public static String getIMEI(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}
