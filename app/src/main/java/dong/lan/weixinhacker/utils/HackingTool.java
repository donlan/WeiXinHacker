package dong.lan.weixinhacker.utils;

import android.text.TextUtils;

import dong.lan.sqlcipher.Secure;

/**
 * Created by 梁桂栋 on 17-3-11 ： 上午11:56.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class HackingTool {


    public static String pair;

    public static String getEnWWD(String imei,String uin){
        String pwd = Secure.MD5(imei+uin);
        if(TextUtils.isEmpty(pwd))
            return null;
        pair = pwd.substring(0,7);
        return pair;
    }
}
