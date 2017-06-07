package dong.lan.sqlcipher.event;

import java.util.ArrayList;
import java.util.Map;

import dong.lan.base.bean.Message;
import dong.lan.base.bean.Peer;

/**
 */

public class HackingEvent {
    public ArrayList<Message> messages;
    public Map<String, Peer> peerMap;
    public long lastTime;

    public HackingEvent(Map<String, Peer> peerMap, ArrayList<Message> messages, long lastTime) {
        this.messages = messages;
        this.lastTime = lastTime;
        this.peerMap = peerMap;
    }
}
