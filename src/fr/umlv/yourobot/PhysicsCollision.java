package fr.umlv.yourobot;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsCollision implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		//System.out.println("===> Contact start");
		/*if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) {
			Element e = (Element)contact.getFixtureA().getBody().getUserData();
			e.getBody().setType(BodyType.STATIC);
		}
		else if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer) {
			Element e = (Element)contact.getFixtureB().getBody().getUserData();
			e.getBody().setType(BodyType.STATIC);
		}*/
	}

	@Override
	public void endContact(Contact contact) {
		//System.out.println("===> Contact ended");
		/*if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) {
			Element e = (Element)contact.getFixtureA().getBody().getUserData();
			e.getBody().setType(BodyType.DYNAMIC);
		}
		else if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer) {
			Element e = (Element)contact.getFixtureB().getBody().getUserData();
			e.getBody().setType(BodyType.DYNAMIC);
		}*/
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) { }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) {
			Element e = (Element)contact.getFixtureA().getBody().getUserData();
			e.setLife((int)(e.getLife()-impulse.normalImpulses[0]));
		}
	}
}
