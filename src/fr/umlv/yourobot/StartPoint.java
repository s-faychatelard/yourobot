package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Objects;

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
			image = Toolkit.getDefaultToolkit().getImage("startPoint.png");
			
		//PolygonShape blockShape;
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position = Objects.requireNonNull(position);
		//blockShape = new PolygonShape();
		//blockShape.setAsBox(STARTPOINT_WIDTH/2, STARTPOINT_HEIGTH/2);

		fixtureDef = null;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(image, (int)p.x , (int)p.y, STARTPOINT_WIDTH, STARTPOINT_HEIGTH, null);
	}

	@Override
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
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
