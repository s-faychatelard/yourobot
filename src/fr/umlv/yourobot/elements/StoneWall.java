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
package fr.umlv.yourobot.elements;

import org.jbox2d.common.Vec2;

public class StoneWall extends Wall {
	
	private static final String imagePath = "stoneWall.png";

	/**
	 * Create a stone wall
	 * 
	 * @param position of the wall
	 */
	public StoneWall(Vec2 position) {
		//Null is test by super
		super(position);
	}

	/**
	 * Return the resource name of the wall
	 */
	@Override
	String getImagePath() {
		return imagePath;
	}
	
	

}
