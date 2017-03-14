package dong.lan.weixinhacker.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午10:51.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {


    private AdapterBinder<T> binder;
    public BaseAdapter(AdapterBinder<T> binder) {
        this.binder = binder;
    }

    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return binder.bindViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.bindData(binder.valueAt(position));
    }

    @Override
    public int getItemCount() {
        return binder.size();
    }
}
