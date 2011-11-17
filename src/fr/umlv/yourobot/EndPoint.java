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

public class EndPoint implements Element {
	
	final static int ENDPOINT_WIDTH = 50;
	final static int ENDPOINT_HEIGTH = ENDPOINT_WIDTH;
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;
	protected Body body;
	private static Image image;
	
	public EndPoint(Vec2 position) {	
		if(image == null)
			image = Toolkit.getDefaultToolkit().getImage("endPoint.png");
			
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(position.x, position.y);

		blockShape = new PolygonShape();
		blockShape.setAsBox(ENDPOINT_WIDTH/2, ENDPOINT_HEIGTH/2);

		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = 0.f;
		fixtureDef.restitution = 0.f;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(image, (int)p.x , (int)p.y, ENDPOINT_WIDTH, ENDPOINT_HEIGTH, null);
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

	@Override
	public void setLife(int life) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLife() {
		// TODO Auto-generated method stub
		return 0;
	}
}