package dong.lan.weixinhacker.presentation;


/**
 * Created by 梁桂栋 on 17-2-9 ： 下午6:33.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ProgressView extends BaseView {

    void alert(String text);

    boolean isProcessing();

    void dismiss();
}
