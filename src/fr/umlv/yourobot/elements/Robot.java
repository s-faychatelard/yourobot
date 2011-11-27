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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.Objects;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.yourobot.physics.World;
import fr.umlv.yourobot.utils.ImageFactory;

public abstract class Robot extends Element {
	private static final int SIZE_OF_TAIL = 8;
	private final static int SIZE = 40;
	protected final static int INITIAL_SPEED = 100; //used by RobotAI
	private CircleShape blockShape;
	private int life;
	private double direction = 0.;
	private final LinkedList<Vec2> tail;
	
	public abstract String getImagePath();

	public Robot(Vec2 position) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = Objects.requireNonNull(position);
		bodyDef.linearDamping = .5f;

		blockShape = new CircleShape();
		blockShape.m_radius = SIZE/2;
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = .8f;
		fixtureDef.restitution = 0.f;
		
		tail = new LinkedList<>();
		tail.add(position);
		life = 100;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		generateTail(graphics);
		Vec2 p = this.body.getPosition();
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(p.x, p.y);
		affineTransform.rotate(Math.toRadians(this.direction), SIZE/2, SIZE/2);
		if(this instanceof RobotFake) {
			graphics.drawImage(ImageFactory.getImage(getImagePath()), affineTransform, null);
		}
		else if(this instanceof RobotAI) {
			graphics.setColor(new Color(.3f, .3f, .3f));
			graphics.fillOval((int)this.body.getPosition().x, (int)this.body.getPosition().y, (int)SIZE, (int)SIZE);
			graphics.setColor(Color.BLACK);
			graphics.fillOval((int)this.body.getPosition().x+2, (int)this.body.getPosition().y+2, (int)SIZE-4, (int)SIZE-4);
		}
		else {
			int x = (int)this.body.getPosition().x;
			int y = (int)this.body.getPosition().y;
			graphics.setColor(Color.GRAY);
			graphics.fillOval(x, y, SIZE, SIZE);
			graphics.setColor(new Color(.9f, .9f, .9f));
			graphics.fillOval(x+2, y+2, SIZE-4, SIZE-4);
			//graphics.drawImage(ImageFactory.getImage(getImagePath()), affineTransform, null);
			graphics.setColor(Color.GRAY);
			graphics.drawRect(x + 10, y - 10, 26, 4);
			if(this.getLife() < 30)
				graphics.setColor(Color.RED);
			else 
				graphics.setColor(Color.GREEN);
			graphics.fillRect(x + 10+1, y - 10+1, (int)(this.getLife() / 4), 3);
		}
	}

	private void generateTail(Graphics2D graphics) {
		if(this instanceof RobotFake || this instanceof RobotAI) return;
		Vec2 lastPosition = tail.peekFirst();
		
		int x = (int)this.getBody().getLinearVelocity().x;
		int y = (int)this.getBody().getLinearVelocity().y;
		double distance = Math.sqrt(x*x + y*y);
		
		int radius = 24;
		float alpha = (float)distance/60;
		for(Vec2 pos : tail) {
			graphics.setColor(new Color(1.f, 1.f, .5f, alpha));
			graphics.fillOval((int)pos.x+SIZE/4, (int)pos.y+SIZE/4, radius, radius);
			radius-=3;
			alpha-=.1f;
			if(alpha<0.f) alpha=0.f;
		}
		
		x = (int)this.getBody().getPosition().x - (int)lastPosition.x;
		y = (int)this.getBody().getPosition().y - (int)lastPosition.y;
		distance = Math.sqrt(x*x + y*y);
		if(distance<10) return;
		
		if(tail.size()==SIZE_OF_TAIL) tail.removeLast();
		tail.addFirst(this.getBody().getPosition().clone());
	}

	protected double getDirection() {
		return this.direction;
	}
	
	protected void setDirection(double direction) {
		this.direction = direction;
	}
	
	public void throttle() {
		if(this.life<=0) return;
		Vec2 vec = new Vec2();
		vec.x = (float) Math.cos(Math.toRadians(direction)) * INITIAL_SPEED;
		vec.y = (float) Math.sin(Math.toRadians(direction)) * INITIAL_SPEED;
		this.body.applyLinearImpulse(vec, this.body.getLocalCenter());
	}

	public void rotate(int rotation) {
		if(this.life<=0) return;
		direction = (direction + rotation)%360;
		if(direction<0) direction = 360+direction;
	}

	public void rotateLeft() {
		rotate(-10);
	}

	public void rotateRight() {
		rotate(10);
	}

	public void setLife(int life) {
		this.life = life;
		if(this.life <= 0) {
			this.body.setLinearVelocity(new Vec2(-this.getBody().getLinearVelocity().x,-this.getBody().getLinearVelocity().y));
			World.checkRobotsLife();
		}
	}

	public int getLife() {
		return this.life;
	}
}
