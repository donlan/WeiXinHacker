package dong.lan.microserver.handler;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import dong.lan.microserver.AppServer.Request;
import dong.lan.microserver.AppServer.ResUriHandler;
import dong.lan.microserver.utils.IOStreamUtils;

public class ResourceUriHandler implements ResUriHandler {

    private static final String TAG = ResourceUriHandler.class.getSimpleName();

    private Activity mActivity;


    public ResourceUriHandler(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean matches(String uri) {
        return uri != null && (uri.startsWith("/app/") || uri.endsWith("favicon.ico") || uri.equals("/"));
    }

    @Override
    public void handler(Request request) {
        String assetPath = request.getUri();
        if(request.getUri().equals("/")){
            assetPath = "index.html";
        }else{
            int i = assetPath.lastIndexOf("?");
            if(i ==-1){
                assetPath = request.getUri().substring(1);
            }else{ //忽略某些页内请求附带的参数
                assetPath = request.getUri().substring(1,i);
            }
        }
        try {
            InputStream is = mActivity.getResources().getAssets().open(assetPath);
            byte[] data = IOStreamUtils.readRawFromStream(is);
            is.close();
            OutputStream outputStream = null;
            PrintStream printStream = null;
            outputStream = request.getUnderlySocket().getOutputStream();
            printStream = new PrintStream(outputStream);
            printStream.println("HTTP/1.1 200 OK");
            printStream.println("Content-Length:" + data.length);

            Log.d(TAG, "handler: "+assetPath+"  -> "+data.length);


            if (assetPath.endsWith(".html")) {
                printStream.println("Content-Type:text/html");
            } else if (assetPath.endsWith(".js")) {
                printStream.println("Content-Type:text/javascript");
            } else if (assetPath.endsWith(".css")) {
                printStream.println("Content-Type:text/css");
            } else if (assetPath.endsWith(".html")) {
                printStream.println("Content-Type:text/html");
            } else if (assetPath.endsWith(".jpg")) {
                printStream.println("Content-Type:image/jpeg");
            } else if (assetPath.endsWith(".png")) {
                printStream.println("Content-Type:image/png");
            } else if (assetPath.endsWith(".json")) {
                printStream.println("Content-Type:application/json");
            } else if (assetPath.endsWith(".ico")) {
                printStream.println("Content-Type:image/x-icon");
            } else if (assetPath.endsWith(".gif")) {
                printStream.println("Content-Type:image/gif");
            } else if (assetPath.endsWith(".svg")) {
                printStream.println("Content-Type:text/xml");
            } else if (assetPath.endsWith(".woff")) {
                printStream.println("Content-Type:application/font-woff");
            } else if (assetPath.endsWith("otf")) {
                printStream.println("Content-Type:application/x-font-otf");
            } else if (assetPath.endsWith(".ttf")) {
                printStream.println("Content-Type:application/x-font-ttf");
            } else {
                printStream.println("Content-Type:application/octet_stream");
            }
            printStream.println();
            printStream.write(data);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void destroy() {
        this.mActivity = null;
    }

}
