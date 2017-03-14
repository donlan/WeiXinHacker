package dong.lan.microserver.handler;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import dong.lan.microserver.AppServer.Request;
import dong.lan.microserver.AppServer.ResUriHandler;
import dong.lan.sqlcipher.Helper;

/**
 * Created by 梁桂栋 on 17-3-13 ： 下午12:07.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class WXDataDeleteHandler implements ResUriHandler {
    private static final String TAG = WXDataDeleteHandler.class.getSimpleName();

    @Override
    public boolean matches(String uri) {
        if (TextUtils.isEmpty(uri))
            return false;
        return uri.startsWith("/delete/");
    }

    @Override
    public void handler(Request request) {
        String msgId = request.getUri().substring("/delete/".length());
        OutputStream outputStream = null;
        PrintStream printStream = null;
        Log.d(TAG, "handler: "+request.getUri());
        try {
            outputStream = request.getUnderlySocket().getOutputStream();
            printStream = new PrintStream(outputStream);
            if (Helper.instance().deleteMessage(msgId))
                printStream.println("HTTP/1.1 200 OK");
            else {
                printStream.println("HTTP/1.1 400 OK");
            }
            printStream.println();
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {

    }
}
