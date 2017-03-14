package dong.lan.weixinhacker.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import dong.lan.sqlcipher.Helper;
import dong.lan.sqlcipher.bean.Message;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.adapter.HistoryAdapter;
import dong.lan.weixinhacker.ui.base.BaseFragment;
import dong.lan.weixinhacker.ui.custom.RecycleViewDivider;
import dong.lan.sqlcipher.SPHelper;

/**
 * Created by 梁桂栋 on 17-3-11 ： 下午8:55.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class HistoryFragment extends BaseFragment {

    public static boolean change = false;
    private String pwd;

    public static BaseFragment newInstance(String tittle) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }

    @BindView(R.id.history_list)
    RecyclerView historyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_history, container, false);
            bindView(content);
            initView();
        }
        return content;
    }


    private HistoryAdapter adapter;

    private void initView() {
        historyList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        pwd = SPHelper.instance().getString("pwd");
        historyList.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 4, Color.GRAY));
        if (TextUtils.isEmpty(pwd)) {
            toast("请先进行微信破解");
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (change) {
            toast("数据已经发生改变,刷新中...");
            change = false;
            List<Message> messages = Helper.instance().getLocalMessages(pwd);
            if (adapter == null) {
                adapter = new HistoryAdapter();
                adapter.init(messages);
                historyList.setAdapter(adapter.build());
            }
        }
    }
}
