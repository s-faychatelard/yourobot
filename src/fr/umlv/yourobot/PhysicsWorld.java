package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	private static World world;
	private static ArrayList<Element> elementList;

	public PhysicsWorld() {
		world = new World(new Vec2(0,0), true);
		world.setContactListener(new PhysicsCollision());
		elementList = new ArrayList<>();
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
		world.step(1/60f, 10, 8);
		graphics.setColor(Color.BLACK);
		for(Element e : elementList) {
			e.draw(graphics);
			//System.out.println(e.getBody().getPosition() +" " + world.getGravity());
		}
	}
}
