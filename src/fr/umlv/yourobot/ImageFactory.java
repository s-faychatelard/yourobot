package fr.umlv.yourobot;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

public class ImageFactory {
	
	private static Map<String, Image> imagePool;
	
	public static Image getImage(String path) {
		if (imagePool == null)
			imagePool = new HashMap<String, Image>();

		Image image = imagePool.get(path);
		
		if (image == null) {
			/** TODO : manage case if image does not exist */
			image = Toolkit.getDefaultToolkit().getImage(path);
			imagePool.put(path, image);
		}
		
		return image;
	}
}
