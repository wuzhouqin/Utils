

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 图片文件类型检测
 * @author Zhouqin.Wu
 *
 */
public class ImageFileDetect {
    /**
     * jpg
     */
    private static byte[] JPEG_PATTERN = new byte[]{(byte)0xFF, (byte)0xD8};
    /**
     * gif
     */
    private static byte[] GIF_PATTERN_1 = new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x39, (byte)0x61};
    /**
     *gif
     */
    private static byte[] GIF_PATTERN_2 = new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x38, (byte)0x61};
    /**
     * png
     */
    private static byte[] PNG_PATTERN = new byte[]{(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A};
    
    /**
     * 文件是否为图片文件(jpeg, png, gif)
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isImage(File file) throws FileNotFoundException {
        FileInputStream input = new FileInputStream(file);
        
        try {
            byte headers[] = new byte[10];
            int length = input.read(headers);
            if (headers.length > length) {
                byte[] t = new byte[length];
                System.arraycopy(headers, 0, t, 0, length);
                headers = t;
            }
            
            if (jpeg(headers)) {
                return true;
            }
            
            if (gif(headers)) {
                return true;
            }
            
            if (png(headers)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        
        return false;
    }
    
    /**
     * 是否GIF
     * @param headers
     * @return
     */
    public static boolean gif(byte[] headers) {
        return match(headers, GIF_PATTERN_1) || match(headers, GIF_PATTERN_2);
    }
    
    /**
     * 是否PNG
     * @param headers
     * @return
     */
    public static boolean png(byte[] headers) {
        return match(headers, PNG_PATTERN);
    }
    
    /**
     * 是否JPEG
     * @param headers
     * @return
     */
    public static boolean jpeg(byte[] headers) {
        return match(headers, JPEG_PATTERN);
    }
    
    /**
     * 匹配
     * @param target
     * @param pattern
     * @return
     */
    private static boolean match(byte[] headers, byte[] pattern) {
        if (headers == null || pattern.length > headers.length)
            return false;
        
        byte[] target = new byte[pattern.length];
        System.arraycopy(headers, 0, target, 0, pattern.length);
        
        for (int i = 0; i < target.length; i ++) {
            if ((target[i] & 0xFF) != (pattern[i] & 0xFF)) {
                return false;
            }
        }
        
        return true;
    }
}
