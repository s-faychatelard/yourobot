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
	final static int INITIAL_SPEED = 100;
	private final static int ROBOT_SIZE = 40;
	private CircleShape blockShape;
	private int life;
	private double direction = 0.;
	private static final int SIZE_OF_TAIL = 5;
	private final LinkedList<Vec2> tail;
	
	public abstract String getImagePath();

	public Robot(Vec2 position) {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = Objects.requireNonNull(position);
		bodyDef.linearDamping = .5f;

		blockShape = new CircleShape();
		blockShape.m_radius = ROBOT_SIZE/2;
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = .8f;
		fixtureDef.restitution = 0.f;
		
		life = 100;
		
		tail = new LinkedList<>();
	}

	@Override
	public void draw(Graphics2D graphics) {
		generateTail(graphics);
		Vec2 p = this.body.getPosition();
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.setToTranslation(p.x, p.y);
		affineTransform.rotate(Math.toRadians(this.direction), ROBOT_SIZE/2, ROBOT_SIZE/2);
		if(this instanceof FakeRobot) {
			graphics.drawImage(ImageFactory.getImage(getImagePath()), affineTransform, null);
		}
		else if(this instanceof RobotIA) {
			graphics.setColor(Color.BLACK);
			graphics.fillOval((int)this.body.getPosition().x, (int)this.body.getPosition().y, (int)ROBOT_SIZE, (int)ROBOT_SIZE);
		}
		else {
			graphics.setColor(Color.WHITE);
			int x = (int)this.body.getPosition().x;
			int y = (int)this.body.getPosition().y;

			graphics.fillOval(x, y, (int)ROBOT_SIZE, (int)ROBOT_SIZE);
			graphics.drawImage(ImageFactory.getImage(getImagePath()), affineTransform, null);
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
		if(this instanceof RobotIA) return;
		Vec2 lastPosition = tail.peekFirst();
		if(lastPosition == null) {
			tail.addLast(this.getBody().getPosition().clone());
			return;
		}
		int x = (int)this.getBody().getPosition().x - (int)lastPosition.x;
		int y = (int)this.getBody().getPosition().y - (int)lastPosition.y;
		double distance = Math.sqrt(x*x + y*y);
		System.out.println(distance);
		if(distance<50) return;
		
		if(tail.size()==SIZE_OF_TAIL) tail.removeFirst();
		tail.addLast(this.getBody().getPosition());
		int radius = 25;
		for(Vec2 pos : tail) {
			System.out.println("plop");
			graphics.fillOval((int)pos.x, (int)pos.y, radius, radius);
			radius-=5;
		}
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
