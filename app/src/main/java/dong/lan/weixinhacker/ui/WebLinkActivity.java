package dong.lan.weixinhacker.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;


import butterknife.BindView;
import dong.lan.microserver.AppServer.ApMgr;
import dong.lan.microserver.AppServer.AndroidMicroServer;
import dong.lan.microserver.Constant;
import dong.lan.microserver.handler.ResourceUriHandler;
import dong.lan.microserver.handler.WXDataDeleteHandler;
import dong.lan.microserver.handler.WXDataQueryHandler;
import dong.lan.microserver.utils.NetUtils;
import dong.lan.microserver.AppServer.WifiAPBroadcastReceiver;
import dong.lan.microserver.AppServer.WifiMgr;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.ui.base.BaseBarActivity;
import dong.lan.weixinhacker.ui.custom.LabelTextView;



public class WebLinkActivity extends BaseBarActivity {


    private static final String TAG = WebLinkActivity.class.getSimpleName();
    private static final int CAN_NOT_OPEN_SERVER_ERROR = 0x0001;
    private static final int GET_AP_IP = 0x0002;


    @BindView(R.id.web_link_address)
    LabelTextView webLinkAddress;
    @BindView(R.id.web_link_ssid)
    LabelTextView webLinkSSID;

    private String pwd;
    private WifiAPBroadcastReceiver mWifiAPBroadcastReceiver;
    private boolean mIsInitialized;
    private AndroidMicroServer mAndroidMicroServer;
    private String ssid;
    private MyHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link);
        bindView("微信消息导出");
        init();

    }


    private void init() {

        webLinkAddress.setText("请耐心等待...");
        webLinkSSID.setText("尝试开启微服务...");
        handler = new MyHandler();
        WifiMgr.getInstance(getApplicationContext()).disableWifi();
        if (ApMgr.isApOn(getApplicationContext())) {
            ApMgr.disableAp(getApplicationContext());
        }

        mWifiAPBroadcastReceiver = new WifiAPBroadcastReceiver() {
            @Override
            public void onWifiApEnabled() {
                Log.i(TAG, "======>>>onWifiApEnabled !!!");
                if (!mIsInitialized) {
                    try {
                        AndroidMicroServer.MAIN_EXECUTOR.execute(createServer());
                        mIsInitialized = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        mIsInitialized = false;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(WifiAPBroadcastReceiver.ACTION_WIFI_AP_STATE_CHANGED);
        registerReceiver(mWifiAPBroadcastReceiver, filter);

        ApMgr.isApOn(getApplicationContext());
        ssid = TextUtils.isEmpty(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 100);
            } else {
                ApMgr.configApState(getApplicationContext(), ssid); // change Ap state :boolean
            }
        }
    }


    /**
     * 创建一个AndroidMicroServer
     *
     * @return
     * @throws Exception
     */
    public Runnable createServer() throws Exception {
        return new Runnable() {
            @Override
            public void run() {
                String hotspotIpAddr = "";
                try {
                    // 确保热点开启之后获取得到IP地址
                    hotspotIpAddr = WifiMgr.getInstance(getApplicationContext()).getHotspotLocalIpAddress();
                    int count = 0;
                    while ((hotspotIpAddr.equals(Constant.DEFAULT_UNKOWN_IP))
                            && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(1000);
                        hotspotIpAddr = WifiMgr.getInstance(getApplicationContext()).getIpAddressFromHotspot();
                        count++;
                    }

                    if (hotspotIpAddr.equals(Constant.INVALID_IP) || hotspotIpAddr.equals(Constant.DEFAULT_UNKOWN_IP)) {
                        hotspotIpAddr = Constant.DEFAULT_SERVER_IP;
                    }
                    // 即使热点wifi的IP地址也是无法连接网络 所以采取此策略
                    count = 0;
                    while (!NetUtils.pingIpAddress(hotspotIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(500);
                        count++;
                    }
                } catch (Exception e) {
                    handler.sendEmptyMessage(CAN_NOT_OPEN_SERVER_ERROR);
                }
                Message message = Message.obtain(handler);
                message.what = GET_AP_IP;
                Bundle data = new Bundle();
                data.putString("address", hotspotIpAddr + ":" + Constant.DEFAULT_MICRO_SERVER_PORT);
                data.putString("ssid", ssid);
                message.setData(data);
                handler.sendMessage(message);
                mAndroidMicroServer = new AndroidMicroServer(Constant.DEFAULT_MICRO_SERVER_PORT);
                mAndroidMicroServer.resgisterResUriHandler(new ResourceUriHandler(WebLinkActivity.this));
                mAndroidMicroServer.resgisterResUriHandler(new WXDataQueryHandler());
                mAndroidMicroServer.resgisterResUriHandler(new WXDataDeleteHandler());
                mAndroidMicroServer.start();
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApMgr.disableAp(getApplicationContext());
        mAndroidMicroServer.stop();
        mAndroidMicroServer.unresgisterResUriHandlerList();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
                ApMgr.configApState(getApplicationContext(), ssid); // change Ap state :boolean
            } else {
                webLinkAddress.setText("无法开启微服务");
                dialog("无法启动热点");
            }
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CAN_NOT_OPEN_SERVER_ERROR:
                    webLinkAddress.setText("无法开启微服务");
                    break;
                case GET_AP_IP:
                    webLinkAddress.setText(msg.getData().getString("address"));
                    webLinkSSID.setText("电脑链接WIFI: " + msg.getData().getString("ssid"));
                    break;

            }
        }
    }


}
