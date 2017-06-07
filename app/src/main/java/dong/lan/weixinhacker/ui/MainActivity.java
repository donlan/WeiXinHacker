package dong.lan.weixinhacker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;
import dong.lan.base.bean.Peer;
import dong.lan.base.bean.User;
import dong.lan.library.LabelTextView;
import dong.lan.sqlcipher.Helper;
import dong.lan.sqlcipher.SPHelper;
import dong.lan.sqlcipher.event.HackingEvent;
import dong.lan.sqlcipher.event.MsgEvent;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.ui.base.BaseActivity;
import dong.lan.weixinhacker.ui.custom.Dialog;
import dong.lan.weixinhacker.utils.HackingTool;
import dong.lan.weixinhacker.utils.IMEIUtil;
import dong.lan.weixinhacker.utils.TimeUtil;

/**
 * 程序主页面
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_last_export_time_tv)
    TextView lastExTimeTv;
    @BindView(R.id.main_status_tv)
    TextView statusTv;
    @BindView(R.id.main_export_ltv)
    LabelTextView exportActionLtv;
    @BindView(R.id.main_user_uin)
    TextView userUinTv;
    private User user;

    //退出登录
    @OnClick(R.id.main_logout)
    public void logout() {
        BmobUser.logOut();
        SPHelper.instance().putString("uin", "");
        finish();
    }


    //执行导出微信消息逻辑
    @OnClick(R.id.main_export_ltv)
    void export() {
        if (!enable) {
            dialog("检测系统uin中...");
            return;
        }

        //计算数据库破解密码
        String pwd = HackingTool.getEnWWD(IMEIUtil.getIMEI(this), user.getUsername());
        if (TextUtils.isEmpty(pwd)) {
            dialog("无法获取破解密码");
            return;
        }
        exportActionLtv.setEnabled(false);
        //开始执行破解流程
        Helper.instance().hackingWX(user.getUsername(), pwd, user.getLastUploadTime());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            lastExTimeTv.setText("上次导出时间：");
            if (user.getLastUploadTime() == 0)
                lastExTimeTv.append("未进行过导出");
            else
                lastExTimeTv.append(TimeUtil.getTime(user.getLastUploadTime(), "yyyy-MM-dd HH:mm"));
            userUinTv.setText(user.getUsername());
        }
        EventBus.getDefault().register(this);
        //破解UIN码
        Helper.instance().hackingUin();
    }


    private boolean enable = false;

    /**
     * 接收EventBus的通知事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(MsgEvent event) {
        statusTv.append(event.data.toString());
        statusTv.append("\n");
        // ==1说明是破解得到UIN码
        if (event.cmd == 1) {
            //当前手机中的UIN码与当前用户不匹配，就退出系统
            if (!event.data.toString().equals(user.getUsername())) {
                new Dialog(this)
                        .setMessageText("系统uin: " + event.data.toString() + " 用户id:" + user.getUsername() + " 不匹配！！！")
                        .setClickListener(new Dialog.DialogClickListener() {
                            @Override
                            public boolean onDialogClick(int which) {
                                BmobUser.logOut();
                                SPHelper.instance().putString("uin", "");
                                finish();
                                return true;
                            }
                        }).show();

            } else {
                enable = true;
            }
        }
    }


    /**
     * 破解完毕通知事件
     *
     * @param event 事件中包含：用户列表，微信消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHackingEvent(final HackingEvent event) {
        if (event.messages == null || event.messages.isEmpty()) {
            exportActionLtv.setEnabled(true);
            toast("无最新的微信消息");
            return;
        }
        statusTv.append("新消息个数：" + event.messages.size());
        statusTv.append("\n");
        List<BmobObject> bmobObjects = new ArrayList<>();
        bmobObjects.addAll(event.messages);
        toast("开始导入服务，请耐心等待");
        //批量插入操作，将数据保存到Bmob
        new BmobBatch().insertBatch(bmobObjects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                exportActionLtv.setEnabled(true);
                if (e == null) {
                    toast("数据上传完毕: " + list.size());
                    user.setLastUploadTime(event.lastTime);
                    user.addAllUnique("talker", event.peerMap.values());
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                } else {
                    toast("数据上传失败：" + e.getMessage());
                }
            }
        });


        //如果破解过程中有用户信息
        if (!event.peerMap.isEmpty()) {

            //1 先获取当前用户的所有会话用户
            BmobQuery<Peer> peerBmobQuery = new BmobQuery<>();
            peerBmobQuery.addWhereEqualTo("owner", user);
            peerBmobQuery.findObjects(new FindListener<Peer>() {
                @Override
                public void done(List<Peer> list, BmobException e) {
                    if (e == null && !list.isEmpty()) {
                        List<BmobObject> peers = new ArrayList<>();
                        //去除掉后台已经存在会话用户
                        for (Peer p : event.peerMap.values()) {
                            if (!event.peerMap.containsKey(p.getId()))
                                peers.add(p);
                        }
                        //如果存在后台不存在的会话用户，就将这些用户保存到后台
                        if (!peers.isEmpty()) {
                            new BmobBatch().insertBatch(peers).doBatch(new QueryListListener<BatchResult>() {
                                @Override
                                public void done(List<BatchResult> list, BmobException e) {
                                    exportActionLtv.setEnabled(true);
                                    if (e == null) {
                                        toast("用户数据上传完毕: " + list.size());
                                    } else {
                                        toast("数据上传失败：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
