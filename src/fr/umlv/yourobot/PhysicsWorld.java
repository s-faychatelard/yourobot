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

	public PhysicsWorld() {
		world = new World(new Vec2(0,0), true);
		world.setContactListener(new PhysicsCollision());
		elementList = new ArrayList<>();

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, 0);
		PolygonShape blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH/2, 20/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = .0f;
		fixtureDef.friction = .0f;
		fixtureDef.restitution = .0f;
		Body elementBody = world.createBody(bodyDef);
		elementBody.createFixture(fixtureDef);
		elementBody.setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(20/2, Main.HEIGHT/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = .0f;
		fixtureDef.friction = .0f;
		fixtureDef.restitution = .0f;
		elementBody = world.createBody(bodyDef);
		elementBody.createFixture(fixtureDef);
		elementBody.setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, Main.HEIGHT);
		blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH/2, 20/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = .0f;
		fixtureDef.friction = .0f;
		fixtureDef.restitution = .0f;
		elementBody = world.createBody(bodyDef);
		elementBody.createFixture(fixtureDef);
		elementBody.setType(BodyType.STATIC);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(Main.WIDTH, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(20/2, Main.HEIGHT/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = .0f;
		fixtureDef.friction = .0f;
		fixtureDef.restitution = .0f;
		elementBody = world.createBody(bodyDef);
		elementBody.createFixture(fixtureDef);
		elementBody.setType(BodyType.STATIC);
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
		world.step(1/10f, 10, 8);
		graphics.setColor(Color.BLACK);
		for(Element e : elementList) {
			e.draw(graphics);
			//System.out.println(e.getBody().getPosition() +" " + world.getGravity());
		}
	}
}
