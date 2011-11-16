package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class StartPoint implements Element {
	
	final static int STARTPOINT_WIDTH = 50;
	final static int STARTPOINT_HEIGTH = STARTPOINT_WIDTH;
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;
	protected Body body;
	private static Image image;
	
	public StartPoint(Vec2 position) {	
		if(image == null)
			image = Toolkit.getDefaultToolkit().getImage("fakeRobot.png");
			
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(position.x, position.y);

		blockShape = new PolygonShape();
		blockShape.setAsBox(STARTPOINT_WIDTH/2, STARTPOINT_HEIGTH/2);

		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = 0.f;
		fixtureDef.restitution = 0.f;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(image, (int)p.x , (int)p.y, STARTPOINT_WIDTH, STARTPOINT_HEIGTH, null);
	}

	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public BodyDef getBodyDef() {
		return bodyDef;
	}

	@Override
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
}