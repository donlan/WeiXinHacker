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

public class IndexResUriHandler implements ResUriHandler {

    private static final String TAG = IndexResUriHandler.class.getSimpleName();

    private Activity mActivity;


    public IndexResUriHandler(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean matches(String uri) {
        return uri == null || uri.equals("") || uri.equals("/");
    }

    @Override
    public void handler(Request request) {
        String indexHtml = null;
        try {
            InputStream is = this.mActivity.getAssets().open("index.html");
            indexHtml = IOStreamUtils.inputStreamToString(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (request.getUnderlySocket() != null && indexHtml != null) {
            OutputStream outputStream = null;
            PrintStream printStream = null;

            try {
                outputStream = request.getUnderlySocket().getOutputStream();
                printStream = new PrintStream(outputStream);
                printStream.println("HTTP/1.1 200 OK");
                printStream.println("Content-Length:" + indexHtml.length());
                printStream.println("Content-Type:text/html");
                printStream.println("Cache-Control:no-cache");
                printStream.println("Pragma:no-cache");
                printStream.println("Expires:0");
                printStream.println();

                indexHtml = convert(indexHtml);


                byte[] bytes = indexHtml.getBytes("UTF-8");
                printStream.write(bytes);

                printStream.flush();
                printStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (outputStream != null) {
                    try {
                        outputStream.close();
                        outputStream = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (printStream != null) {
                    try {
                        printStream.close();
                        printStream = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public void destroy() {
        this.mActivity = null;
    }

    public String convert(String indexHtml) {
        return indexHtml;
    }
}
