package dong.lan.microserver.AppServer;

import java.net.Socket;
import java.util.HashMap;

public class Request {

    private String mUri;
    private HashMap<String, String> mHeaderMap = new HashMap<String, String>();
    private Socket mUnderlySocket;


    public Request() {

    }

    public Socket getUnderlySocket() {
        return mUnderlySocket;
    }

    public void setUnderlySocket(Socket mUnderlySocket) {
        this.mUnderlySocket = mUnderlySocket;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String mUri) {
        this.mUri = mUri;
    }

    public void addHeader(String key, String value) {
        this.mHeaderMap.put(key, value);
    }
}
