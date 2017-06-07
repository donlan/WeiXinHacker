package dong.lan.microserver.handler;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import dong.lan.base.controller.JSON;
import dong.lan.microserver.AppServer.AndroidMicroServer;
import dong.lan.microserver.AppServer.Request;
import dong.lan.microserver.AppServer.ResUriHandler;
import dong.lan.sqlcipher.Helper;
import dong.lan.sqlcipher.bean.Message;

/**
 * Created by 梁桂栋 on 17-3-13 ： 下午12:07.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class WXDataQueryHandler implements ResUriHandler {


    @Override
    public boolean matches(String uri) {
        if (TextUtils.isEmpty(uri))
            return false;
        return uri.startsWith("/query/");
    }

    @Override
    public void handler(Request request) {
        if (request.getUri().equals("/query/")) {
            List<Message> messages = Helper.instance().getLocalMessages(AndroidMicroServer.pair);
            int size = messages == null ? 0 : messages.size();
            String jsonStr = "{\"data\":" + JSON.get().gson().toJson(messages) + ",\"size\":" + size + "}";
            OutputStream outputStream = null;
            PrintStream printStream = null;
            try {
                outputStream = request.getUnderlySocket().getOutputStream();
                printStream = new PrintStream(outputStream);
                byte[] data = jsonStr.getBytes();
                printStream.println("HTTP/1.1 200 OK");
                printStream.println("Content-Length:" + data.length);
                printStream.println("Content-Type:application/json");
                printStream.println();
                printStream.write(data);
                printStream.flush();
                printStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void destroy() {

    }
}
