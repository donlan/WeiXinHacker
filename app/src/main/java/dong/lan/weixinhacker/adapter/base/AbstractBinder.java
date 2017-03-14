package dong.lan.weixinhacker.adapter.base;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-11 ： 下午11:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public abstract class AbstractBinder<T> implements AdapterBinder<T> {

    private boolean isCacheShow;
    private List<T> data;
    private List<T> cache;
    protected BinderClickListener<T> clickListener;

    public void showCacheData(boolean isCacheShow) {
        this.isCacheShow = isCacheShow;
    }


    @Override
    public void initCache(List<T> cacheData) {
        cache = cacheData;
    }

    @Override
    public void init(List<T> data) {
        this.data = data;
    }

    @Override
    public T valueAt(int position) {
        if (isCacheShow)
            return cache.get(position);
        return data.get(position);
    }

    @Override
    public int size() {
        if (isCacheShow)
            return cache == null ? 0 : cache.size();
        return data == null ? 0 : data.size();
    }

    @Override
    public BaseAdapter<T> build() {
        return new BaseAdapter<>(this);
    }


}
