package fr.umlv.yourobot.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageFactory {
	
	private final static Map<String, Image> imagePool = new HashMap<String, Image>();
	
	public static Image getImage(String path) {
		Objects.requireNonNull(path);
		Image image = imagePool.get(path);
		if (image == null) {
			//TODO manage case if image does not exist
			image = Toolkit.getDefaultToolkit().getImage(path);
			imagePool.put(path, image);
		}
		return image;
	}
}
