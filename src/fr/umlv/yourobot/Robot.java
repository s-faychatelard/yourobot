package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Robot implements Element {
	private final static int ROBOT_WIDTH = 20;
	private final static int ROBOT_HEIGTH = ROBOT_WIDTH;
	private final static int INITIAL_SPEED = 1000000;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Body body;
	private static BufferedImage image;

	private double direction = 0.;

	public Robot(Vec2 position) {	
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(position.x, position.y);


		blockShape = new PolygonShape();
		blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2);

		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = .8f;
		fixtureDef.restitution = .1f;
	}

	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
		//float angle = (float) Math.toDegrees(body.getAngle());
		graphics.setColor(Color.BLACK);
		graphics.fillRect((int)p.x, (int)p.y, ROBOT_WIDTH, ROBOT_HEIGTH);
		/*if(image==null) {
			try {
				File f = new File("/Users/sylvain/Documents/Projets/yourobot/robot.png");
				System.out.println(f);
				image = ImageIO.read(f);
			} catch (IOException e) {
			}
		}
		System.out.println(image);
		graphics.drawImage(image, (int)p.x, (int)p.y, null);*/
	}

	public void rotateLeft() {
		//TODO get direction from this.body.getVelocity()
		//System.out.println(Math.atan2(this.body.getLinearVelocity().x, this.body.getLinearVelocity().y)/3.14*180+180);
		direction = (direction - 10.)%360;
		if(direction<0) direction = 360;
		//System.out.println(direction);
		Vec2 vec = new Vec2();
		vec.x = (float)Math.cos(direction/180*3.14)*INITIAL_SPEED;
		vec.y = (float)Math.sin(direction/180*3.14)*INITIAL_SPEED;
		this.body.setLinearVelocity(vec);
	}

	public void rotateRight() {
		//TODO get direction from this.body.getVelocity()
		//System.out.println(Math.atan2(this.body.getLinearVelocity().x, this.body.getLinearVelocity().y)/3.14*180+180);
		direction = (direction + 10.)%360;
		//System.out.println(direction);
		Vec2 vec = new Vec2();
		vec.x = (float)Math.cos(direction/180*3.14)*INITIAL_SPEED;
		vec.y = (float)Math.sin(direction/180*3.14)*INITIAL_SPEED;
		this.body.setLinearVelocity(vec);
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}

	@Override
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
