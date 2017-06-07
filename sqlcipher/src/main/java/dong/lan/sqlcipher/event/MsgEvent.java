package dong.lan.sqlcipher.event;

/**
 */

public class MsgEvent {
   public int cmd;
    public Object data;

    public MsgEvent(int cmd, Object data) {
        this.cmd = cmd;
        this.data = data;
    }
}
