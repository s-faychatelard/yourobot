package fr.umlv.yourobot.elements;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Element {
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	Body body;
	
	public abstract void draw(Graphics2D graphics);
	
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
		//Setup volume friction
		this.body.setLinearDamping(.5f);	
	}
	
	public Body getBody() {

		return this.body;
	}
	
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}
	
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
