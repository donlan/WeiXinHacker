package dong.lan.sqlcipher;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobUser;
import dong.lan.base.bean.Peer;
import dong.lan.base.bean.User;
import dong.lan.sqlcipher.event.HackingEvent;
import dong.lan.sqlcipher.event.MsgEvent;
import dong.lan.sqlcipher.event.UinEvent;

/**
 * 数据库破解帮助类
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

    /**
     * 破解UIN
     */
    public void hackingUin() {
        EventBus.getDefault().post(new MsgEvent(0, "自动检索微信UIN..."));
        //uin路径
        String path = "/data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml";
        //先将uin的文件复制到SDCARD
        RootCMD.execRootCmd("cp " + path + " /sdcard/wx_uin.xml");

        File file = new File("/sdcard/wx_uin.xml");
        if (file.exists()) {
            try {
                //读取文件
                char[] buf = new char[256];
                FileReader r = new FileReader(file);
                StringBuilder sb = new StringBuilder();
                while (r.read(buf) != -1) {
                    sb.append(buf);
                }
                String s = sb.toString();
                if (s.contains("default_uin")) {
                    //找到UIN出现的位置
                    int i = s.indexOf("default_uin") + 20;
                    sb.delete(0, sb.length());
                    //识别微信UIN
                    while (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                        sb.append(s.charAt(i));
                        i++;
                    }
                    String uin = sb.toString();

                    SPHelper.instance().putString("uin", uin);
                    EventBus.getDefault().post(new MsgEvent(0, "找到UIN:" + uin));
                    EventBus.getDefault().post(new MsgEvent(1, uin));
                    EventBus.getDefault().post(new UinEvent(0, uin));
                }
            } catch (IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new MsgEvent(0, "找不到默认的UIN..."));
            }
        } else {
            EventBus.getDefault().post(new MsgEvent(0, "找不到默认的UIN..."));
        }

    }

    /**
     * 破解微信数据库
     *
     * @param uin      uin码
     * @param password 密码
     * @param lastTime 上次导出的时间
     */
    public void hackingWX(final String uin, final String password, final long lastTime) {
        if (isHacking)
            return;
        isHacking = true;
        //获取数据库存在的目标的路径
        final String dbDir = "/data/data/com.tencent.mm/MicroMsg/" + Secure.MD5("mm" + uin) + "/EnMicroMsg.db";
        EventBus.getDefault().post(new MsgEvent(0, "目标数据库文件：" + dbDir));
        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                EventBus.getDefault().post(new MsgEvent(0, "复制数据库文件..."));
                //复制数据库文件到sdcard
                RootCMD.execRootCmd("cp -R " + dbDir + " /sdcard/");
                EventBus.getDefault().post(new MsgEvent(0, new File(Config.LOCAL_DB_COPY_PATH).getAbsolutePath()));
                File file = new File(Config.LOCAL_DB_PATH);
                if (!file.exists())
                    RootCMD.execRootCmd("cp " + Config.LOCAL_DB_COPY_PATH + " " + Config.LOCAL_DB_PATH);
                //数据破解声明
                SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
                    public void preKey(SQLiteDatabase database) {
                    }

                    public void postKey(SQLiteDatabase database) {
                        database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
                    }
                };

                try {

                    //打开数据库
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(Config.LOCAL_DB_COPY_PATH, password, null, SQLiteDatabase.OPEN_READWRITE, hook);
                    //查询消息时间大于上次导出时间的消息
                    Cursor c = db.query("message", null, "createTime>?", new String[]{"" + lastTime}, null, null, "createTime desc");

                    EventBus.getDefault().post(new MsgEvent(0, "开始解析信息..."));
                    EventBus.getDefault().post(new MsgEvent(0, "最新消息时间：" + TimeUtil.longToString(lastTime, "yyyy.MM.dd hh:mm:ss")));
                    long time = 0;
                    ArrayList<dong.lan.base.bean.Message> messages = new ArrayList<>();
                    User me = BmobUser.getCurrentUser(User.class);
                    Map<String, Peer> peerMap = new HashMap<>();
                    //遍历每个消息
                    while (c.moveToNext()) {
                        //生成消息
                        dong.lan.base.bean.Message message = new dong.lan.base.bean.Message(c);

                        if (time < message.getCreateTime()) {
                            time = message.getCreateTime();
                        }
                        message.setOwner(me);
                        message.parse();
                        messages.add(message);
                        peerMap.put(message.getTalker(), new Peer(me, message.getTalker(), message.getTalker()));
                        EventBus.getDefault().post(new MsgEvent(0, "消息时间：" + TimeUtil.longToString(message.getCreateTime(), "yyyy.MM.dd hh:mm:ss")));

                    }

                    EventBus.getDefault().post(new HackingEvent(peerMap, messages, time));
                    EventBus.getDefault().post(new MsgEvent(0, "解析完成"));
                    c.close();
                    db.close();
                } catch (Exception e) {
                    EventBus.getDefault().post(new MsgEvent(0, "解析异常：" + e.getMessage()));
                    EventBus.getDefault().post(new MsgEvent(0, "可能原因：1.手机没有ROOT \n2.开启飞行模式 \n3.手机没有安装微信"));
                    e.printStackTrace();
                    EventBus.getDefault().post(new HackingEvent(null, null, 0));
                }
                isHacking = false;
            }
        });
    }

}
