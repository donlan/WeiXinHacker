package dong.lan.microserver.AppServer;

public interface ResUriHandler {

    /**
     * is matches the specify uri
     * @param uri
     * @return
     */
    boolean matches(String uri);


    /**
     * handler the request which matches the uri
     * @param request
     */
    void handler(Request request);

    /**
     * releas some resource when finish the handler
     */
    void destroy();
}
