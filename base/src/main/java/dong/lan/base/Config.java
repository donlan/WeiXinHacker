package dong.lan.base;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 */

public class Config {

    public static void init(Context appContext) {
        Bmob.initialize(appContext, "8ef19c712922ccf4e5683f34eacc3359");
    }
}
