package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Wall implements Element {
	final static int WALL_WIDTH = 50;
	final static int WALL_HEIGTH = WALL_WIDTH;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	protected Body body;
	private static BufferedImage image;
	private int life = 100;


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
		fixtureDef.restitution = 0.f;
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
	
	public abstract int attackWithIce();
	public abstract int attackWithStone();
	public abstract int attackWithWood();
	public abstract Image getImage();
	public abstract void setImage(Image img);
	
	@Override
	public void draw(Graphics2D graphics) {

		Vec2 p = this.body.getPosition();
		//Load only once the picture
		if(getImage()==null) {
			setImage(Toolkit.getDefaultToolkit().getImage("iceWall.jpg"));
		}
		
		System.out.println(getImage().getWidth(null));
	    graphics.drawImage(getImage(), (int)p.x, (int)p.y, WALL_WIDTH, WALL_HEIGTH, null);
	    //graphics.fillRect((int)p.x, (int)p.y, WALL_HEIGTH, WALL_WIDTH);
		
		
		//Vec2 p = this.body.getPosition();
		//float angle = (float) Math.toDegrees(body.getAngle());
		//graphics.setColor(Color.MAGENTA);
		
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

}
