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

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Element {
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	Body body;
	
	/**
	 * At refresh time, draw method of all the elements of the world are called
	 * in order to draw their graphic representation
	 * @param graphics to draw
	 */
	public abstract void draw(Graphics2D graphics);
	
	/**
	 * When you call addElement in World class, it set the body reference in this class.
	 * Also set the damping of all element
	 * 
	 * @param body reference
	 */
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
		//Setup volume friction
		this.body.setLinearDamping(.5f);	
	}
	
	/**
	 * @return body, the body of the element
	 */
	public Body getBody() {
		return this.body;
	}
	
	/**
	 * @return bodyDef, the bodyDef of the element
	 */
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}
	
	/**
	 * @return fixtureDef, the fixtureDef of the element
	 */
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
