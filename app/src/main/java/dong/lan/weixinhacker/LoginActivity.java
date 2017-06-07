package dong.lan.weixinhacker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import dong.lan.base.bean.User;
import dong.lan.library.LabelTextView;
import dong.lan.sqlcipher.Helper;
import dong.lan.sqlcipher.SPHelper;
import dong.lan.sqlcipher.event.MsgEvent;
import dong.lan.sqlcipher.event.UinEvent;
import dong.lan.weixinhacker.ui.MainActivity;
import dong.lan.weixinhacker.ui.base.BaseActivity;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_uin_et)
    EditText uinEt;
    @BindView(R.id.login_pwd_et)
    EditText pwdEt;
    @BindView(R.id.login_action)
    LabelTextView login;

    @BindView(R.id.login_hint)
    TextView hintTv;
    private User user;

    //用户执行登录
    @OnClick(R.id.login_action)
    public void loginAction() {
        String password = pwdEt.getText().toString();
        if (TextUtils.isEmpty(password))
            dialog("密码不能为空");
        else {

            user.setUsername(uinEt.getText().toString());
            user.setPassword(password);
            login();
        }
    }

    private void login() {
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Logger.d(e.getErrorCode() + "," + e.getMessage());
                    if (e.getErrorCode() == 101) {//未注册就登陆，则进行自动注册
                        signUp();
                    } else {
                        dialog("登录失败：" + e.getMessage());
                    }
                }
            }
        });
    }

    //注册流程
    private void signUp() {
        user.setLastUploadTime((long) 0);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    login(); //注册成功就自动登录
                } else {
                    Logger.d(e.getErrorCode() + "," + e.getMessage());
                    dialog("注册失败：" + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindView();


        if (SPHelper.instance().getBoolean("firstLogin")) {
            hintTv.setText("首次进入系统会自动为你解析UIN，然后输入的密码将会用来进行用户注册，下次再进入系统直接输入密码即可");
            SPHelper.instance().putBoolean("firstLogin", true);
        }

        EventBus.getDefault().register(this);

        ArrayList<String> per = new ArrayList<>(5);
        per.add(Manifest.permission.READ_PHONE_STATE);
        per.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        per.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        per.add(Manifest.permission.ACCESS_WIFI_STATE);
        per.add(Manifest.permission.CHANGE_WIFI_STATE);
        String uin = SPHelper.instance().getString("uin");

        if (TextUtils.isEmpty(uin)) {
            //进行UIN破解
            Helper.instance().hackingUin();
        } else {
            uinEt.setText(uin);
        }
        user = new User();


    }


    /**
     * 接受破解过程的通知事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MsgEvent(MsgEvent event) {
        hintTv.setText(event.data.toString());
    }

    /**
     * 接收破解的到的UIN码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MsgEvent(UinEvent event) {
        uinEt.setText(event.uin);
        SPHelper.instance().putString("uin", event.uin);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
