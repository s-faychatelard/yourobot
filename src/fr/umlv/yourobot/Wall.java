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

public class Wall implements Element {
	final static int WALL_WIDTH = 20;
	final static int WALL_HEIGTH = WALL_WIDTH;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Body body;
	private static BufferedImage image;

	public Wall(Vec2 position) {	
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(position.x, position.y);

		blockShape = new PolygonShape();
		blockShape.setAsBox(WALL_WIDTH/2, WALL_HEIGTH/2);

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
		graphics.setColor(Color.MAGENTA);
		graphics.fillRect((int)p.x, (int)p.y, WALL_HEIGTH, WALL_WIDTH);
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
