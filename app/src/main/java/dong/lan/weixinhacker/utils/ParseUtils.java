package dong.lan.weixinhacker.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by 梁桂栋 on 17-4-7 ： 下午10:58.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public final class ParseUtils {

    private ParseUtils() {
        //no instance
    }


    public static String msgImageUrl(String content){
        int i = content.indexOf("cdnurl");
        int j = content.indexOf("designerid");
        if(i>0 && j>i){
            Logger.d(content.substring(i+8,j-2).replace("*#*",":"));
            return content.substring(i+8,j-2).replace("*#*",":");
        }
        return "";
    }


    public static String parseMMReader(String content) {
        int i = content.indexOf("<mmreader>");
        int j = content.indexOf("</mmreader>")+11;
        return content.substring(i,j);
    }

    public static String parseLocation(String content) {
        int i = content.indexOf("label")+7;
        int j = content.indexOf("poiname")-2;
        return content.substring(i,j);
    }
}
