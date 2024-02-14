package supernotes.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtils {
    public static byte[] imageToByteArray(String imagePath) {
        byte[] imageBytes = null;
        try (FileInputStream fis = new FileInputStream(new File(imagePath));
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
    
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
    
            imageBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }
    
}

