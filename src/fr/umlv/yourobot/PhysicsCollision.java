package fr.umlv.yourobot;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsCollision implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		//System.out.println("===> Contact start");
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) {
			System.out.println("Collision IA avant");
			Element e = (Element)contact.getFixtureA().getBody().getUserData();
			e.getBody().setType(BodyType.STATIC);
		}
		else if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer) {
			System.out.println("Collision IA avant");
			Element e = (Element)contact.getFixtureB().getBody().getUserData();
			e.getBody().setType(BodyType.STATIC);
		}
	}

	@Override
	public void endContact(Contact contact) {
		//System.out.println("===> Contact ended");
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) {
			System.out.println("Collision IA après");
			Element e = (Element)contact.getFixtureA().getBody().getUserData();
			e.getBody().setType(BodyType.DYNAMIC);
		}
		else if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer) {
			System.out.println("Collision IA après");
			Element e = (Element)contact.getFixtureB().getBody().getUserData();
			e.getBody().setType(BodyType.DYNAMIC);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}
}
