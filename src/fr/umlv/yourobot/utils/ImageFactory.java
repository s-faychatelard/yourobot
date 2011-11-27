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

public class ImageFactory {
	
	private final static Map<String, Image> imagePool = new HashMap<String, Image>();
	
	public static Image getImage(String path) {
		Objects.requireNonNull(path);
		
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getResourcesFolder());
		sb.append(path);
		//System.out.println(Paths.get(sb.toString()));
		Image image = imagePool.get(sb.toString());
		if (image == null) {
			image = Toolkit.getDefaultToolkit().getImage(sb.toString());
			imagePool.put(path, Objects.requireNonNull(image));
		}
		return image;
	}
}
