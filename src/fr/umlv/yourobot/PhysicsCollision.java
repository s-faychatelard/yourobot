package fr.umlv.yourobot;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsCollision implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		System.out.println("===> Contact start");
	}

	@Override
	public void endContact(Contact contact) {
		System.out.println("===> Contact ended");
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}
}
