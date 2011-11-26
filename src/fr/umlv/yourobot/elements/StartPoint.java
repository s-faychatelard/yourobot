package fr.umlv.yourobot.elements;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import fr.umlv.yourobot.utils.ImageFactory;

public class StartPoint extends Element {
	
	private final static int STARTPOINT_SIZE = 50;
	private static final String imagePath = "startPoint.png";
	
	public StartPoint(Vec2 position) {
			
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
	    graphics.drawImage(ImageFactory.getImage(imagePath), (int)p.x , (int)p.y, STARTPOINT_SIZE, STARTPOINT_SIZE, null);
	}
}
