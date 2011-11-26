package fr.umlv.yourobot.physics;

import java.util.Random;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.elements.Bonus;
import fr.umlv.yourobot.elements.EndPoint;
import fr.umlv.yourobot.elements.RobotIA;
import fr.umlv.yourobot.elements.RobotPlayer;

public class Collision implements ContactListener {

	private final static Random rand = new Random();

	@Override
	public void beginContact(Contact contact) {
		//RobotIA ignore EndPoint
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotIA)) {
			return;
		}
		//End RobotIA ignore EndPoint

		//RobotIA ignore Bonus
		if((contact.getFixtureA().getBody().getUserData() instanceof Bonus && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) ||
				(contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotIA)) {
			return;
		}
		//End RobotIA ignore Bonus

		//RobotPlayer take Bonus
		if((contact.getFixtureA().getBody().getUserData() instanceof Bonus && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) ||
				(contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			return;
		}
		//End RobotPlayer take Bonus

		//WIN 
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			Main.Win();
			return;
		}
		//End WIN

		//RobotPlayer collide by RobotIA
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) {
			RobotPlayer robotPlayer = (RobotPlayer)contact.getFixtureA().getBody().getUserData();
			RobotIA robotIA = (RobotIA)contact.getFixtureB().getBody().getUserData();
			Vec2 vecPlayer = robotPlayer.getBody().getLinearVelocity();
			Vec2 vecIA = robotIA.getBody().getLinearVelocity();
			int x = (int)(vecPlayer.x - vecIA.x);
			int y = -(int)(vecPlayer.y - vecIA.y);
			double hypo = Math.sqrt(x*x + y*y);
			System.out.println("Life decrease : " + (robotPlayer.getLife()-hypo/2));
			robotPlayer.setLife((int)(robotPlayer.getLife()-hypo/2));
		}
		//End RobotPlayer collide by RobotIA
		//RobotIA collide something turn it
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotIA) {
			RobotIA robotIA = (RobotIA)contact.getFixtureA().getBody().getUserData();
			if(rand.nextBoolean())
				robotIA.rotate(rand.nextInt(180));
			else
				robotIA.rotate(-rand.nextInt(180));
		}
		if(contact.getFixtureB().getBody().getUserData() instanceof RobotIA) {
			RobotIA robotIA = (RobotIA)contact.getFixtureB().getBody().getUserData();
			if(rand.nextBoolean())
				robotIA.rotate(rand.nextInt(180));
			else
				robotIA.rotate(-rand.nextInt(180));
		}
		//End RobotIA collide something turn it
	}

	@Override
	public void endContact(Contact contact) { }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		//RobotPlayer take Bonus
		if((contact.getFixtureA().getBody().getUserData() instanceof Bonus && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer)) {
			RobotPlayer rp = (RobotPlayer) contact.getFixtureB().getBody().getUserData();
			rp.takeBonus((Bonus)contact.getFixtureA().getBody().getUserData());
			contact.setEnabled(false);
			return;
		}
		if((contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			RobotPlayer rp = (RobotPlayer) contact.getFixtureA().getBody().getUserData();
			rp.takeBonus((Bonus)contact.getFixtureB().getBody().getUserData());
			contact.setEnabled(false);
			return;
		}
		//End RobotPlayer take Bonus
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotIA)) {
			//RobotIA can pass over the endPoint
			contact.setEnabled(false);
			return;
		}
		if((contact.getFixtureA().getBody().getUserData() instanceof Bonus && contact.getFixtureB().getBody().getUserData() instanceof RobotIA) ||
				(contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotIA)) {
			//RobotIA can pass over the Bonus
			contact.setEnabled(false);
			return;
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }
}
