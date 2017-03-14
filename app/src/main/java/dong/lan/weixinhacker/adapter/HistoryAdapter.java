package dong.lan.weixinhacker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.sqlcipher.bean.Message;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.adapter.base.AbstractBinder;
import dong.lan.weixinhacker.adapter.base.BaseHolder;
import dong.lan.weixinhacker.adapter.base.BinderClickListener;
import dong.lan.weixinhacker.ui.custom.LabelTextView;
import dong.lan.weixinhacker.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 17-3-12 ： 上午11:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class HistoryAdapter extends AbstractBinder<Message> {


    @Override
    public BaseHolder<Message> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hisory,null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<Message> clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends BaseHolder<Message>{

        @BindView(R.id.username)
        TextView sender;
        @BindView(R.id.talker)
        TextView receiver;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.content_image)
        ImageView contentImage;
        @BindView(R.id.time)
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindData(Message message) {
            if(message.isSend == 0){
                sender.setText(message.talker);
                receiver.setText("自己");
            }else {
                sender.setText("自己");
                receiver.setText(message.talker);
            }
            content.setText(message.content);
            time.setText(TimeUtil.getTime(message.createTime, "yyyy.MM.dd HH:mm"));
        }
    }
}
