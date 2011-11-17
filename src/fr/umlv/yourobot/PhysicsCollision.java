package fr.umlv.yourobot;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsCollision implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotIA)) {
			//TODO passer au travers
			System.out.println("RobotIA collide with end point");
			return;
		}
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			//TODO WIN
			System.out.println("WIN");
			return;
		}
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) {
			RobotPlayer robotPlayer = (RobotPlayer)contact.getFixtureA().getBody().getUserData();
			RobotIA robotIA = (RobotIA)contact.getFixtureB().getBody().getUserData();
			Vec2 vecPlayer = robotPlayer.getBody().getLinearVelocity();
			Vec2 vecIA = robotIA.getBody().getLinearVelocity();
			System.out.println(vecPlayer + " " + vecIA);
			int x = (int)(vecPlayer.x - vecIA.x);
			int y = -(int)(vecPlayer.y - vecIA.y);
			double hypo = Math.sqrt(x*x + y*y);
			robotPlayer.setLife((int)(robotPlayer.getLife()-hypo/2));
		}
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA) {
			RobotIA robotIA = (RobotIA)contact.getFixtureA().getBody().getUserData();
			robotIA.rotate(180);
			robotIA.jumpTo(null);
		}
		if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA) {
			RobotIA robotIA = (RobotIA)contact.getFixtureB().getBody().getUserData();
			robotIA.rotate(180);
			robotIA.jumpTo(null);
		}
	}

	@Override
	public void endContact(Contact contact) { }
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) { }

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }
}
