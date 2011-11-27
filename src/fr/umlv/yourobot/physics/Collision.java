/**
 * ESIPE Project - IR2 2011/2012 - Advanced Java
 * Copyright (C) 2011 ESIPE - Universite Paris-Est Marne-la-Vallee 
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * Please see : http://www.gnu.org/licenses/gpl.html
 * 
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
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
import fr.umlv.yourobot.elements.RobotAI;
import fr.umlv.yourobot.elements.RobotPlayer;

public class Collision implements ContactListener {

	private final static Random rand = new Random();

	@Override
	public void beginContact(Contact contact) {
		//RobotIA ignore EndPoint
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotAI) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotAI)) {
			return;
		}
		//End RobotIA ignore EndPoint

		//WIN 
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotPlayer) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			Main.Win();
			return;
		}
		//End WIN

		//RobotPlayer collide by RobotIA
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer && contact.getFixtureB().getBody().getUserData() instanceof RobotAI) {
			RobotPlayer robotPlayer = (RobotPlayer)contact.getFixtureA().getBody().getUserData();
			RobotAI robotIA = (RobotAI)contact.getFixtureB().getBody().getUserData();
			Vec2 vecPlayer = robotPlayer.getBody().getLinearVelocity();
			Vec2 vecIA = robotIA.getBody().getLinearVelocity();
			int x = (int)(vecPlayer.x - vecIA.x);
			int y = -(int)(vecPlayer.y - vecIA.y);
			double hypo = Math.sqrt(x*x + y*y);
			robotPlayer.setLife((int)(robotPlayer.getLife()-hypo/2));
		}
		//End RobotPlayer collide by RobotIA
		//RobotIA collide something turn it
		if(contact.getFixtureA().getBody().getUserData() instanceof RobotAI) {
			RobotAI robotIA = (RobotAI)contact.getFixtureA().getBody().getUserData();
			if(rand.nextBoolean())
				robotIA.rotate(rand.nextInt(180));
			else
				robotIA.rotate(-rand.nextInt(180));
		}
		if(contact.getFixtureB().getBody().getUserData() instanceof RobotAI) {
			RobotAI robotIA = (RobotAI)contact.getFixtureB().getBody().getUserData();
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
		//TODO seems never catch
		if((contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotPlayer)) {
			RobotPlayer rp = (RobotPlayer) contact.getFixtureA().getBody().getUserData();
			rp.takeBonus((Bonus)contact.getFixtureB().getBody().getUserData());
			contact.setEnabled(false);
			return;
		}
		//End RobotPlayer take Bonus
		if((contact.getFixtureA().getBody().getUserData() instanceof EndPoint && contact.getFixtureB().getBody().getUserData() instanceof RobotAI) ||
				(contact.getFixtureB().getBody().getUserData() instanceof EndPoint && contact.getFixtureA().getBody().getUserData() instanceof RobotAI)) {
			//RobotIA can pass over the endPoint
			contact.setEnabled(false);
			return;
		}
		if((contact.getFixtureA().getBody().getUserData() instanceof Bonus && contact.getFixtureB().getBody().getUserData() instanceof RobotAI) ||
				(contact.getFixtureB().getBody().getUserData() instanceof Bonus && contact.getFixtureA().getBody().getUserData() instanceof RobotAI)) {
			//RobotIA can pass over the Bonus
			contact.setEnabled(false);
			return;
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { }
}
