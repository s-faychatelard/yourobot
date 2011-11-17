package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Bonus implements Element {
	final static int BONUS_WIDTH = 50;
	final static int BONUS_HEIGTH = BONUS_WIDTH;
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;
	protected Body body;

	public  Bonus(Vec2 position) {	
			
			PolygonShape blockShape;

			bodyDef = new BodyDef();
			bodyDef.type = BodyType.STATIC;
			bodyDef.position = Objects.requireNonNull(position);

			blockShape = new PolygonShape();
			blockShape.setAsBox(BONUS_WIDTH/2, BONUS_HEIGTH/2);

			fixtureDef = new FixtureDef();
			fixtureDef.shape = blockShape;
			fixtureDef.density = 0.f;
			fixtureDef.friction = 0.f;
			fixtureDef.restitution = 0.f;
			
	}

	@Override
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
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
	
	public abstract String getImagePath();
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(getImagePath()), (int)p.x , (int)p.y, BONUS_WIDTH, BONUS_HEIGTH, null);
	}
}
