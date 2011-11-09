package fr.umlv.yourobot;

import java.awt.Graphics2D;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public interface Element {
	public void draw(Graphics2D graphics);
	public void setBody(Body body);
	public Body getBody();
	public BodyDef getBodyDef();
	public FixtureDef getFixtureDef();
}
