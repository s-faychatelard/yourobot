package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

public class StartPoint extends Element {
	
	final static int STARTPOINT_WIDTH = 50;
	final static int STARTPOINT_HEIGTH = STARTPOINT_WIDTH;
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
}
