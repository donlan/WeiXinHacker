package dong.lan.weixinhacker.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import java.util.List;

import cn.bmob.v3.BmobUser;
import dong.lan.sqlcipher.RootCMD;
import dong.lan.weixinhacker.LoginActivity;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.ui.base.BaseActivity;
import pub.devrel.easypermissions.EasyPermissions;

/**
 */

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        String pers[] = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET
        };
        if (EasyPermissions.hasPermissions(this, pers)) {
            run();
        } else {
            EasyPermissions.requestPermissions(this, "需要读取内存卡信息的权限", 1, pers);
        }
    }

    private void run() {
        handler = new Handler();
        if (RootCMD.haveRoot()) {
            if (BmobUser.getCurrentUser() != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                setContentView(R.layout.activity_splash);
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        } else {
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
        run();
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    private class Handler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } else {
                dialog("你的手机没有ROOT，无法使用本功能！");
            }
        }
    }
}
