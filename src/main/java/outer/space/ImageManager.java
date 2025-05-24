package outer.space;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.net.URL;

/**
 * Utility class for dealing with images
 */
public class ImageManager {

    /**
     * Retrieves an image from the resource path
     * @param resourcePath path like "/images/myImage.png"
     * @return an {@code Image} object, or null if not found
     */
    public static Image get_image(String resourcePath) {
        URL url = ImageManager.class.getResource(resourcePath);
        if (url != null) {
            return new ImageIcon(url).getImage();
        } else {
            System.err.println("Image not found: " + resourcePath);
            return null;
        }
    }
}