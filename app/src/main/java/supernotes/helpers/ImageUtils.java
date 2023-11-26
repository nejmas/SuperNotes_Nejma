package supernotes.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtils {
    public static byte[] imageToByteArray(String imagePath) {
        byte[] imageBytes = null;
        try {
            File file = new File(imagePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            imageBytes = bos.toByteArray();

            fis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }
}

