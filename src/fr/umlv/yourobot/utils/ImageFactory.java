/**
 * ESIPE Project - IR2 2011/2012 - Advanced Java
 * Copyright (C) 2011 ESIPE - Universite Paris-Est Marne-la-Vallee 
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * Please see : http://www.gnu.org/licenses/gpl.html
 * 
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
package fr.umlv.yourobot.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Group images in one class
 * Contains access to all images needed for the game
 */
public class ImageFactory {
	
	/**
	 * Contains all loaded images
	 */
	private final static Map<String, Image> imagePool = new HashMap<String, Image>();
	
	/**
	 * Give a path and the corresponding image
	 * 
	 * @param path is the generally the filename
	 * @return the image corresponding to the path
	 */
	public static Image getImage(String path) {
		Objects.requireNonNull(path);
		Image image = imagePool.get(path);
		if (image == null) {
			image = Toolkit.getDefaultToolkit().getImage(ImageFactory.class.getClass().getResource("/resources/"+path));
			imagePool.put(path, Objects.requireNonNull(image));
		}
		return image;
	}
}
