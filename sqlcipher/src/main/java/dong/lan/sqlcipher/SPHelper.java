package dong.lan.sqlcipher;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by 梁桂栋 on 17-1-10 ： 下午10:35.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class SPHelper {
    private SharedPreferences preferences;
    private Context context;
    private static final String DEFAULT_SP_NAME = "smartTrip";

    private static SPHelper spHelper = null;

    public static SPHelper instance() {
        if (spHelper == null)
            spHelper = new SPHelper();
        return spHelper;
    }

    public void init(Context context, String spName) {
        this.context  = context;
        preferences = context.getSharedPreferences(
                TextUtils.isEmpty(spName) ? DEFAULT_SP_NAME : spName, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences(String name){
        return context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }
    private SPHelper() {
    }


    public void putString(String key, String val) {
        if (key == null || val == null)
            return;
        preferences.edit().putString(key, val).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }

    public void putInt(String key, int val) {
        if (key == null)
            return;
        preferences.edit().putInt(key, val).apply();
    }


    public long getLong(String key) {
        return preferences.getLong(key, -1);
    }

    public void putLong(String key, long val) {
        if (key == null)
            return;
        preferences.edit().putLong(key, val).apply();
    }


    public void putBoolean(String key, boolean val) {
        if (key == null)
            return;
        preferences.edit().putBoolean(key, val).apply();
    }

    public String getString(String key) {
        if (key == null)
            return "";
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }


}
