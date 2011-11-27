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

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.utils.ImageFactory;

public abstract class Bonus extends Element {
	final static int QUARTER_DIAGONAL = (int)Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;
	private static final int BONUS_SIZE = 40;
	private boolean isTaken;

	/**
	 * Construct a BodyDef and FixtureDef for the Bonus that will be used in Jbox2D world
	 * @param position
	 */
	public Bonus(Vec2 position) {	
			
			PolygonShape blockShape;

			bodyDef = new BodyDef();
			bodyDef.type = BodyType.STATIC;
			bodyDef.position = Objects.requireNonNull(position);

			blockShape = new PolygonShape();
			blockShape.setAsBox(BONUS_SIZE/2, BONUS_SIZE/2);

			fixtureDef = new FixtureDef();
			fixtureDef.shape = blockShape;
			fixtureDef.density = 0.f;
			fixtureDef.friction = 0.f;
			fixtureDef.restitution = 0.f;
			
			isTaken=false;
	}

	
	public abstract Bonus update();
	
	/**
	 * @return String the path (from the resources folder) to the image 
	 * representing the bonus
	 */
	public abstract String getImagePath();
	
	public abstract void execute(RobotPlayer robot);
	
	/** 
	 * @return the duration of the bonus effect. Return 0 if the bonus has not duration (ex : bombers)
	 */
	public abstract int getDuration();

	
	public void setTaken() {
		isTaken=true;
	}
	
	public boolean isTaken() {
		return this.isTaken;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		if(isTaken) return;
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(getImagePath()), (int)p.x , (int)p.y, BONUS_SIZE, BONUS_SIZE, null);
		
	    graphics.setFont(new Font("Verdana", Font.BOLD, 10));
	    if(getDuration() != 0)
	    	graphics.drawString(getDuration()+"s", (int)p.x, (int)p.y);
	}
}
