package dong.lan.weixinhacker.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午11:00.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface AdapterBinder<T> {

    void init(List<T> data);

    void initCache(List<T> cacheData);

    BaseHolder<T> bindViewHolder(ViewGroup parent, int viewType);

    T valueAt(int position);

    BaseAdapter<T> build();

    int size();

    void showCacheData(boolean isCacheShow);

    void setBinderClickListener(BinderClickListener<T> clickListener);
}
