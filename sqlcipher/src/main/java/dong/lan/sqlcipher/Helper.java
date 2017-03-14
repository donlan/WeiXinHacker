package dong.lan.sqlcipher;

import android.content.ContentValues;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dong.lan.sqlcipher.bean.Message;

/**
 * Created by 梁桂栋 on 17-3-11 ： 下午11:11.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class Helper {

    private static final String TAG = Helper.class.getSimpleName();
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private boolean isHacking = false;
    private SQLiteDatabase wxDB;

    private Helper() {

    }

    private static Helper instance;

    public static Helper instance() {
        if (instance == null)
            instance = new Helper();
        return instance;
    }


    public boolean isHacking() {
        return isHacking;
    }


    public List<Message> getLocalMessages(String password) {
        if (wxDB == null || !wxDB.isOpen()) {
            SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
                public void preKey(SQLiteDatabase database) {
                }

                public void postKey(SQLiteDatabase database) {
                    database.rawExecSQL("PRAGMA cipher_migrate;");
                }
            };
            wxDB = SQLiteDatabase.openDatabase(Config.LOCAL_DB_PATH, password, null, SQLiteDatabase.OPEN_READWRITE, hook);
        }
        final List<Message> messages = new ArrayList<>();
        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Cursor c = wxDB.query("message", null, null, null, null, null, "talker,createTime desc");
                    while (c.moveToNext()) {
                        Message msg = new Message(c);
                        messages.add(msg);
                    }
                    c.close();
                } catch (Exception e) {
                    wxDB.close();
                }
            }
        });
        return messages;
    }

    public boolean deleteMessage(String msgId) {
        if (wxDB.isOpen()) {
            try {
                wxDB.execSQL("delete from message where msgId=\"" + msgId + "\"");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void hackingWXDB(final String uin, final String password) {
        if (isHacking)
            return;
        isHacking = true;
        final String dbDir = "/data/data/com.tencent.mm/MicroMsg/" + Secure.MD5("mm" + uin) + "/EnMicroMsg.db";

        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                RootCMD.execRootCmd("cp -R " + dbDir + " /sdcard/");
                File file = new File("");
                if (!file.exists())
                    RootCMD.execRootCmd("cp " + Config.LOCAL_DB_COPY_PATH + " /sdcard/wx.db");


                SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
                    public void preKey(SQLiteDatabase database) {
                    }

                    public void postKey(SQLiteDatabase database) {
                        database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
                    }
                };

                try {

                    if (wxDB == null) {
                        wxDB = SQLiteDatabase.openDatabase(Config.LOCAL_DB_PATH, password, null, SQLiteDatabase.OPEN_READWRITE, hook);
                    }

                    SQLiteDatabase db = SQLiteDatabase.openDatabase(Config.LOCAL_DB_COPY_PATH, password, null, SQLiteDatabase.OPEN_READWRITE, hook);
                    Cursor c = db.query("message", null, null, null, null, null, "talker,createTime desc");
                    long lastTime = SPHelper.instance().getLong(Config.DB_QUERY_LAST_TIME);
                    long time = 0;
                    while (c.moveToNext()) {
                        Message msg = new Message(c);
                        if (msg.createTime > time)
                            time = msg.createTime;
                        if (time > lastTime) {
                            try {
                                wxDB.insert("message", null, msg.toContentValues());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    SPHelper.instance().putLong(Config.DB_QUERY_LAST_TIME, time);
                    c.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isHacking = false;
            }
        });
    }
}
