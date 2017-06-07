package dong.lan.weixinhacker;

import android.app.Application;

import com.orhanobut.logger.Logger;

import net.sqlcipher.database.SQLiteDatabase;

import dong.lan.base.Config;
import dong.lan.sqlcipher.SPHelper;

/**
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        SPHelper.instance().init(this,"wxhacking");
        Logger.init("WX").methodCount(3);
        Config.init(this);
    }
}
