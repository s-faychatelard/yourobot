package fr.umlv.yourobot.elements;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.yourobot.utils.ImageFactory;

public class EndPoint extends Element {
	
	private final static int ENDPOINT_RADIUS = 40/2;
	private static final String imagePath = "endPoint.png";
	
	public EndPoint(Vec2 position) {
			
		CircleShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position = Objects.requireNonNull(position);
		
		blockShape = new CircleShape();
		blockShape.m_radius = ENDPOINT_RADIUS;
		//blockShape.setAsBox(ENDPOINT_WIDTH/2, ENDPOINT_HEIGTH/2);

		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = 0.f;
		fixtureDef.restitution = 0.f;
		fixtureDef.filter.categoryBits = 3;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(imagePath), (int)p.x , (int)p.y, ENDPOINT_RADIUS*2, ENDPOINT_RADIUS*2, null);
	}
}