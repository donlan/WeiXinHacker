package dong.lan.sqlcipher;

import android.util.Log;


import com.orhanobut.logger.Logger;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            try {
                wxDB = SQLiteDatabase.openDatabase(Config.LOCAL_DB_PATH, password, null, SQLiteDatabase.OPEN_READWRITE, hook);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void hackingUin() {
        EventBus.getDefault().post(new MsgEvent(0, "自动检索微信UIN..."));
        String path = "/data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml";
        RootCMD.execRootCmd("cp " + path + " /sdcard/wx_uin.xml");

        File file = new File("/sdcard/wx_uin.xml");
        if (file.exists()) {
            try {
                char[] buf = new char[256];
                FileReader r = new FileReader(file);
                StringBuilder sb = new StringBuilder();
                while (r.read(buf) != -1) {
                    sb.append(buf);
                }
                String s = sb.toString();
                if (s.contains("default_uin")) {
                    int i = s.indexOf("default_uin") + 20;
                    SPHelper.instance().putString("uin", s.substring(i, i + 9));
                    EventBus.getDefault().post(new MsgEvent(0, "找到UIN:" + s.substring(i, i + 9)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new MsgEvent(0, "找不到默认的UIN..."));
            }
        } else {
            EventBus.getDefault().post(new MsgEvent(0, "找不到默认的UIN..."));
        }

    }

    public void hackingWXDB(final String uin, final String password) {
        if (isHacking)
            return;
        isHacking = true;
        final String dbDir = "/data/data/com.tencent.mm/MicroMsg/" + Secure.MD5("mm" + uin) + "/EnMicroMsg.db";

        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                EventBus.getDefault().post(new MsgEvent(0, "复制数据库文件..."));
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
                    String[] s = c.getColumnNames();
                    for (int i = 0; i < s.length; i++)
                        Log.d(TAG, "run: " + s[i]);
                    EventBus.getDefault().post(new MsgEvent(0, "开始解析信息..."));
                    int count = 1;
                    while (c.moveToNext()) {
                        Message msg = new Message(c);
                        Logger.d(msg);
                        if (msg.createTime > time)
                            time = msg.createTime;
                        if (time > lastTime) {
                            try {
                                wxDB.insert("message", null, msg.toContentValues());
                                EventBus.getDefault().post(new MsgEvent(0, "解析信息:" + (count++)) + " -> " + msg.msgId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    EventBus.getDefault().post(new MsgEvent(0, "解析完成"));
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
