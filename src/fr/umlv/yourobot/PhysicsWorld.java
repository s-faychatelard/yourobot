package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	private static World world;
	private static ArrayList<Element> elementList;
	Body []limits;

	public PhysicsWorld() {
		world = new World(new Vec2(0,0), true);
		world.setContactListener(new PhysicsCollision());
		elementList = new ArrayList<>();

		//TODO transform limits has Wall but keep this position and size
		limits = new Body[4];
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(0, -15);
		PolygonShape blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH, 15/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[0] = world.createBody(bodyDef);
		limits[0].createFixture(fixtureDef);
		limits[0].setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(-15, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(15/2, Main.HEIGHT);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[1] = world.createBody(bodyDef);
		limits[1].createFixture(fixtureDef);
		limits[1].setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(0, Main.HEIGHT);
		blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH, 15/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[2] = world.createBody(bodyDef);
		limits[2].createFixture(fixtureDef);
		limits[2].setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(Main.WIDTH, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(15/2, Main.HEIGHT);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[3] = world.createBody(bodyDef);
		limits[3].createFixture(fixtureDef);
		limits[3].setType(BodyType.STATIC);
	}
	
	public static ArrayList<Element> getAllElement() {
		return elementList;
	}
	
	public Element addElement(Element element) {
		Body elementBody = world.createBody(element.getBodyDef());
		elementBody.createFixture(element.getFixtureDef());
		elementBody.setType(element.getBodyDef().type);
		element.setBody(elementBody);
		elementList.add(element);
		return element;
	}

	public void render(Graphics2D graphics) {
		world.step(1/10f, 15, 8);
		graphics.setColor(Color.BLACK);
		for(Element e : elementList) {
			e.draw(graphics);
			//System.out.println(e.getBody().getPosition() +" " + world.getGravity());
		}
		graphics.fillRect((int)limits[0].getPosition().x, (int)limits[0].getPosition().y, Main.WIDTH, 15);
		graphics.fillRect((int)limits[1].getPosition().x, (int)limits[1].getPosition().y, 15, Main.HEIGHT);
		graphics.fillRect((int)limits[2].getPosition().x, (int)limits[2].getPosition().y, Main.WIDTH, 15);
		graphics.fillRect((int)limits[3].getPosition().x, (int)limits[3].getPosition().y, 15, Main.HEIGHT);
	}
}
