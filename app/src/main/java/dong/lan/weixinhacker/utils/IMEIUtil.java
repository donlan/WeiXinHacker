package dong.lan.weixinhacker.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by 梁桂栋 on 17-3-11 ： 上午10:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class IMEIUtil {

    public static String getIMEI(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}
