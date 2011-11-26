package fr.umlv.yourobot.elements;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.utils.ImageFactory;

public abstract class Bonus extends Element {
	final static int QUARTER_DIAGONAL = (int)Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;
	private final static int BONUS_SIZE = 40;
	private boolean isTaken;

	public  Bonus(Vec2 position) {	
			
			PolygonShape blockShape;

			bodyDef = new BodyDef();
			bodyDef.type = BodyType.STATIC;
			bodyDef.position = Objects.requireNonNull(position);

			blockShape = new PolygonShape();
			blockShape.setAsBox(BONUS_SIZE/2, BONUS_SIZE/2);

			fixtureDef = new FixtureDef();
			fixtureDef.shape = blockShape;
			fixtureDef.density = 0.f;
			fixtureDef.friction = 0.f;
			fixtureDef.restitution = 0.f;
			
			isTaken=false;
	}
	
	public abstract String getImagePath();
	public abstract void execute(RobotPlayer robot);
	
	public void setTaken() {
		isTaken=true;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		if(isTaken) return;
		Vec2 p = this.body.getPosition();
	    graphics.drawImage(ImageFactory.getImage(getImagePath()), (int)p.x , (int)p.y, BONUS_SIZE, BONUS_SIZE, null);
	}
}
