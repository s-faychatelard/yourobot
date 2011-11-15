package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
	    graphics.drawImage(getImage(), (int)p.x, (int)p.y, WALL_WIDTH, WALL_HEIGTH, null);
		}
		
	public int getLife() {
		return life;
	}
		
	public void setLife(int life) {
		this.life = life;
	}
		
		

}
