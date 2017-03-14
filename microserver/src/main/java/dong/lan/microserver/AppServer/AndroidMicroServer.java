package dong.lan.microserver.AppServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dong.lan.microserver.SLog;
import dong.lan.microserver.utils.IOStreamUtils;

/**
 * The micro server in Android
 */
public class AndroidMicroServer {

    private static final String TAG = AndroidMicroServer.class.getSimpleName();
    /**
     * 主要的线程池
     */
    public static Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(5);

    public static String pair;

    /**
     * the server port
     */
    private int mPort;

    /**
     * the server socket
     */
    private ServerSocket mServerSocket;

    /**
     * the thread pool which handle the incoming request
     */
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();

    /**
     * uri router handler
     */
    private List<ResUriHandler> mResUriHandlerList = new ArrayList<ResUriHandler>();


    /**
     * the flag which the micro server enable
     */
    private boolean mIsEnable = true;


    public AndroidMicroServer(int port) {
        this.mPort = port;
    }

    /**
     * register the resource uri handler
     *
     * @param resUriHandler
     */
    public void resgisterResUriHandler(ResUriHandler resUriHandler) {
        this.mResUriHandlerList.add(resUriHandler);
    }


    /**
     * unresigter all the resource uri hanlders
     */
    public void unresgisterResUriHandlerList() {
        for (ResUriHandler resUriHandler : mResUriHandlerList) {
            resUriHandler.destroy();
            resUriHandler = null;
        }
    }


    /**
     * start the android micro server
     */
    public void start() {
        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mServerSocket = new ServerSocket(mPort);
                    while (mIsEnable) {
                        Socket socket = mServerSocket.accept();
                        hanlderSocketAsyn(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * stop the android micro server
     */
    public void stop() {
        if (mIsEnable) {
            mIsEnable = false;
        }

        unresgisterResUriHandlerList();

        if (mServerSocket != null) {
            try {
                mServerSocket.close();
                mServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * handle the incoming socket
     *
     * @param socket
     */
    private void hanlderSocketAsyn(final Socket socket) {
//        mThreadPool.submit(new Runnable() {
//            @Override
//            public void run() {
                Request request = createRequest(socket);

                for (ResUriHandler resUriHandler : mResUriHandlerList) {
                    if (!resUriHandler.matches(request.getUri())) {
                        continue;
                    }
                    resUriHandler.handler(request);
                }
//            }
//        });
    }

    /**
     * create the requset object by the specify socket
     *
     * @param socket
     * @return
     */
    private Request createRequest(Socket socket) {
        Request request = new Request();
        request.setUnderlySocket(socket);
        try {
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            InputStream is = socket.getInputStream();
            String requestLine = IOStreamUtils.readLine(is);
            SLog.i(TAG, socketAddress + ":requestLine->" + requestLine);
            String requestType = requestLine.split(" ")[0];
            String requestUri = requestLine.split(" ")[1];

            //解决URL中文乱码的问题
            requestUri = URLDecoder.decode(requestUri, "UTF-8");

            request.setUri(requestUri);


            String header = "";
            while ((header = IOStreamUtils.readLine(is)) != null) {
                String headerKey = header.split(":")[0];
                String headerVal = header.split(":")[1];
                request.addHeader(headerKey, headerVal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;
    }

}
