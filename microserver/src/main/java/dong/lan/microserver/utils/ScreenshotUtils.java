package dong.lan.microserver.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

/**
 * 缩略图工具类
 */
public class ScreenshotUtils {

    /**
     * 创建缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap createVideoThumbnail(String filePath){
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
        return bitmap;
    }


    /**
     * 将图片转换成指定宽高
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    public static Bitmap extractThumbnail(Bitmap source, int width, int height){
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(source, width, height);
        return bitmap;
    }


}
