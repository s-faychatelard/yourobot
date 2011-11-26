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
	
	public abstract void draw(Graphics2D graphics);
	
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
		//Setup volume friction
		this.body.setLinearDamping(.5f);	
	}
	
	public Body getBody() {

		return this.body;
	}
	
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}
	
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
