package dong.lan.weixinhacker.adapter.base;

/**
 * Created by 梁桂栋 on 17-2-25 ： 下午11:11.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface BinderClickListener<T> {

    public static final int ACTION_DELETE = 0x100;
    public static final int ACTION_CLICK = 0x101;

    void onClick(T data, int position, int action);
}
