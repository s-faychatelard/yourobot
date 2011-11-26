package fr.umlv.yourobot.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.yourobot.utils.ImageFactory;

public abstract class Robot extends Element {
	final static int INITIAL_SPEED = 50;
	private final static int ROBOT_SIZE = 40;
	private CircleShape blockShape;
	private int life;
	private double direction = 0.;

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
	}

	@Override
	public void draw(Graphics2D graphics) {
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
			graphics.fillOval((int)this.body.getPosition().x, (int)this.body.getPosition().y, (int)ROBOT_SIZE, (int)ROBOT_SIZE);
			graphics.drawImage(ImageFactory.getImage(getImagePath()), affineTransform, null);
		}
	}
	
	public double getDirection() {
		return this.direction;
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

	public void jumpToDetectedRobot(Vec2 vec) {
		Objects.requireNonNull(vec);
		Vec2 p1 = this.getBody().getPosition();
		Vec2 p2 = vec;
		int x = (int)(p2.x - p1.x);
		int y = -(int)(p2.y - p1.y);
		double hypo = Math.sqrt(x*x + y*y);
		direction = Math.toDegrees(Math.acos(y/hypo));
		if(x<0)
			direction = 360 - direction;
		direction = direction - 90;
		if(direction<0) direction = 360+direction;
		vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(direction))*INITIAL_SPEED*500;
		vec.y = (float)Math.sin(Math.toRadians(direction))*INITIAL_SPEED*500;
		this.body.setLinearVelocity(vec);
	}

	public void setLife(int life) {
		this.life = life;
		if(this.life <= 0) {
			this.body.setLinearVelocity(new Vec2(-this.getBody().getLinearVelocity().x,-this.getBody().getLinearVelocity().y));
			//TODO dead
		}
	}

	public int getLife() {
		return this.life;
	}
}
