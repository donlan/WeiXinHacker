package dong.lan.microserver.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOStreamUtils {


    public static byte[] readRawFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte buf[] = new byte[10240];
        int readed;
        while((readed = is.read(buf)) > 0){
            bos.write(buf,0,readed);
        }
        return bos.toByteArray();
    }

    public static String readLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int a = 0, b = 0;
        while((b != -1) && !(a == '\r' && b == '\n')){
            a = b;
            b = is.read();
            sb.append((char)(b));
        }

        String line = sb.toString();

        if(line.equals("\r\n")){
            return null;
        }

        return line;
    }


    public static String inputStreamToString(InputStream is){
        if(is == null){
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] bytes = new byte[2048];
        try {
            while((len = is.read(bytes)) != -1){
                baos.write(bytes, 0, len);
            }

            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
