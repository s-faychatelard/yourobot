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

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RobotAIRaycastCallback implements RayCastCallback {
	private int count;

	/**
	 * Initialize the Callback
	 * Must be call before every raycast()
	 */
	public void init() {
		count=0;
	}

	/**
	 * When a fixture is detect during raycast() this method is called
	 */
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		count++;
		return 1f;
	}
	
	/**
	 * Get the number of object detect by the raycast()
	 * 
	 * @return the number of report element
	 */
	public int getCount() {
		return this.count;
	}
};
