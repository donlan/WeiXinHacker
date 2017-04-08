package dong.lan.weixinhacker.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.sqlcipher.bean.Message;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.adapter.base.AbstractBinder;
import dong.lan.weixinhacker.adapter.base.BaseHolder;
import dong.lan.weixinhacker.adapter.base.BinderClickListener;
import dong.lan.weixinhacker.ui.custom.LabelTextView;
import dong.lan.weixinhacker.utils.ParseUtils;
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

        @BindView(R.id.msg_type)
        LabelTextView type;
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
            contentImage.setVisibility(View.GONE);
            content.setText("");
            if(message.type == Message.TYPE_BIG_EMOJI){
                contentImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(ParseUtils.msgImageUrl(message.content))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.one)
                        .error(R.drawable.two)
                        .into(contentImage);
                content.setVisibility(View.VISIBLE);
                type.setText("图片表情");

            }else if(message.type == Message.TYPE_LOCATION){
                contentImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(R.drawable.location_mag)
                        .into(contentImage);
                content.setVisibility(View.VISIBLE);
                content.setText(ParseUtils.parseLocation(message.content));
                type.setText("位置");
            }else if(message.type == Message.TYPE_PLANE_TEXT){
                content.setVisibility(View.VISIBLE);
                contentImage.setVisibility(View.GONE);
                content.setText(message.content);
                type.setText("文本");
            }else if(message.type == Message.TYPE_SECURE){
                content.setVisibility(View.VISIBLE);
                contentImage.setVisibility(View.GONE);
                content.setText(ParseUtils.parseMMReader(message.content));
                type.setText("微信团队");
            }else if(message.type == Message.TYPE_VOICE){
                type.setText("语音");
                content.setVisibility(View.VISIBLE);
                contentImage.setVisibility(View.GONE);
                content.setText(message.content);
            }else{
                type.setText("未知");
                content.setVisibility(View.VISIBLE);
                contentImage.setVisibility(View.GONE);
                content.setText(message.content);
            }
            time.setText(TimeUtil.getTime(message.createTime, "yyyy.MM.dd HH:mm"));
        }
    }
}
