package supernotes.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
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
            LOGGER.error("Une erreur s'est produite.", e);
        }
        return imageBytes;
    }
    
}

