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

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import fr.umlv.yourobot.utils.ImageFactory;

public class StartPoint extends Element {
	
	private final static int SIZE = 50;
	private static final String imagePath = "startPoint.png";
	
	public StartPoint(Vec2 position) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position = Objects.requireNonNull(position);
		fixtureDef = null;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(imagePath), (int)p.x , (int)p.y, SIZE, SIZE, null);
	}
}
