package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class Robot implements Element {
	private final static int ROBOT_WIDTH = 20;
	private final static int ROBOT_HEIGTH = ROBOT_WIDTH;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Body body;
	
	public Robot(Vec2 position) {	
		PolygonShape blockShape;

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(position.x, position.y);


		blockShape = new PolygonShape();
		blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2);

		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = 1.f;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
		//float angle = (float) Math.toDegrees(body.getAngle());
		graphics.setColor(Color.BLACK);
		graphics.fillRect((int)p.x, (int)p.y, ROBOT_WIDTH, ROBOT_HEIGTH);
	}
	
	public void translate(Vec2 vec) {
			this.body.applyForce(vec, this.body.getLocalCenter());
		//if(this.body.getContactList()==null)
			//this.body.setTransform(new Vec2(this.body.getPosition().x + vec.x, this.body.getPosition().y + vec.y), 0);
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
}
