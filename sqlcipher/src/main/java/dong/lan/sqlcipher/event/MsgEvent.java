package dong.lan.sqlcipher;

/**
 * Created by 梁桂栋 on 17-4-5 ： 下午3:37.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class MsgEvent {
   public int cmd;
    public Object data;

    public MsgEvent(int cmd, Object data) {
        this.cmd = cmd;
        this.data = data;
    }
}
