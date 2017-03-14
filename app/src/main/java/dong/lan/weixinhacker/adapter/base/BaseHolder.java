package dong.lan.weixinhacker.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 梁桂栋 on 17-2-5 ： 上午12:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public abstract class BaseHolder<T>  extends RecyclerView.ViewHolder{

    private T data;

    public T value(){
        return data;
    }
    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T t);
}
