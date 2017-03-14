package dong.lan.weixinhacker.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.permission.CallBack;
import dong.lan.permission.Permission;
import dong.lan.sqlcipher.Helper;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.ui.base.BaseFragment;
import dong.lan.weixinhacker.utils.HackingTool;
import dong.lan.weixinhacker.utils.IMEIUtil;
import dong.lan.sqlcipher.RootCMD;
import dong.lan.sqlcipher.SPHelper;

/**
 * Created by 梁桂栋 on 17-3-11 ： 下午8:55.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class HackingFragment extends BaseFragment {
    private static final String TAG = HackingFragment.class.getSimpleName();
    private String pwd;

    public static BaseFragment newInstance(String tittle) {
        HackingFragment fragment = new HackingFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }


    @BindView(R.id.wx_uin_et)
    EditText weUinEt;
    private ArrayList<String> per;
    @BindView(R.id.hacking_progress)
    TextView hackingResultTv;


    @OnClick(R.id.uin_guider)
    void howToGetUin() {
        Intent linkIntent = new Intent(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse("http://blog.csdn.net/yuanbohx/article/details/41280837");
        linkIntent.setData(content_url);
        startActivity(linkIntent);
    }

    @OnClick(R.id.startHacker)
    void startHacker() {
        if (Helper.instance().isHacking())
            return;
        if (!RootCMD.haveRoot()) {
            dialog("你的手机没有root权限无法破解");
            return;
        }
        final String uin = weUinEt.getText().toString();
        if (TextUtils.isEmpty(uin)) {
            dialog("uin码不能为空");
            return;
        }
        SPHelper.instance().putString("uin", uin);
        hackingResultTv.setText("");

        pwd = HackingTool.getEnWWD(IMEIUtil.getIMEI(getContext()), uin);
        if (TextUtils.isEmpty(pwd)) {
            dialog("无法获取破解密码");
            return;
        }
        SPHelper.instance().putString("pwd",pwd);
        Permission.instance().check(new CallBack<List<String>>() {
            @Override
            public void onResult(List<String> result) {
                if (result == null) {
                    hackingResultTv.append("开始破解...\n");
                    hacking(uin);
                } else {
                    dialog("无法获取权限");
                }
            }
        }, getActivity(), per);

    }


    private void hacking(final String uin) {
        hackingResultTv.append("开始复制MicroMsg文件夹下的消息文件...\n");

        Helper.instance().hackingWXDB(uin, pwd);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_hacking, container, false);
            bindView(content);
            initView();
        }
        return content;
    }

    private void initView() {
        weUinEt.setText(SPHelper.instance().getString("uin"));
        per = new ArrayList<>(5);
        per.add(Manifest.permission.READ_PHONE_STATE);
        per.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        per.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        per.add(Manifest.permission.ACCESS_WIFI_STATE);
        per.add(Manifest.permission.CHANGE_WIFI_STATE);

        Permission.instance().check(new CallBack<List<String>>() {
            @Override
            public void onResult(List<String> result) {

            }
        }, getActivity(), per);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permission.instance().handleRequestResult(getActivity(),requestCode,permissions,grantResults);
    }
}
