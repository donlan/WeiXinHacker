package dong.lan.weixinhacker;

import android.app.Application;

import net.sqlcipher.database.SQLiteDatabase;

import dong.lan.sqlcipher.SPHelper;

/**
 * Created by 梁桂栋 on 17-3-11 ： 上午11:54.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        SPHelper.instance().init(this,"wxhacking");
    }
}
