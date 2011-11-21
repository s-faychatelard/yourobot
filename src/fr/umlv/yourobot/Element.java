package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.util.Objects;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Element {
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected Body body;
	
	public abstract void draw(Graphics2D graphics);
	
	public void setBody(Body body) {
		this.body = Objects.requireNonNull(body);
	}
	
	public Body getBody() {
		//Setup volume friction
		this.body.setLinearDamping(.5f);
		return this.body;
	}
	
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}
	
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
