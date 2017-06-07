package dong.lan.sqlcipher;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 */

public class Secure {
    public static String MD5(String origin){
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(origin.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }

    public static String SHA1(String origin){
        if (origin == null)
            return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] ori = origin.getBytes();
            digest.update(ori);
            byte[] enc = digest.digest();
            return Base64.encodeToString(enc, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
