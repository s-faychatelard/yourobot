package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Wall extends Element {
	final static int WALL_WIDTH = 50;
	final static int WALL_HEIGTH = WALL_WIDTH;
	private int life = 100;


	public Wall(Vec2 position) {	
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position = Objects.requireNonNull(position);
		blockShape = new PolygonShape();
		blockShape.setAsBox(WALL_WIDTH/2, WALL_HEIGTH/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = .8f;
		fixtureDef.restitution = 0.f;
	}
	
	public abstract int attackWithIce();
	public abstract int attackWithStone();
	public abstract int attackWithWood();
	abstract String getImagePath();
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(getImagePath()), (int)p.x , (int)p.y, WALL_WIDTH, WALL_HEIGTH, null);
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
}
