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
package fr.umlv.yourobot.elements;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.World;

public class RobotPlayer extends Robot {

	private static final String imagePath = "robot.png";
	private final LinkedList<Bonus> bonus;
	private Bonus currentExecutedBonus=null;

	/**
	 * Create a player 
	 * 
	 * @param position of the player
	 */
	public RobotPlayer(Vec2 position) {
		//Null is test by super
		super(position);
		bonus = new LinkedList<>();
	}

	/**
	 * Take a bonus and add it to the bonuses queue
	 * 
	 * @param bonus
	 */
	public void takeBonus(Bonus bonus) {
		if(bonus.isTaken()) return;
		bonus.setTaken();
		World.removeBody(bonus);
		this.bonus.push(bonus);
	}

	/**
	 * Try to use a bonus if there is one in the queue
	 */
	public void useBonus() {
		if(this.getLife()<=0) return;
		if(currentExecutedBonus!=null) return;
		if(bonus.size()<=0) return;
		currentExecutedBonus = bonus.pop();
		currentExecutedBonus.execute(this);
	}

	/**
	 * Return the resource name of the fake robot
	 */
	@Override
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Try to update a bonus if there is one in execution
	 */
	public void update() {
		if(currentExecutedBonus==null) return;
		currentExecutedBonus = currentExecutedBonus.update();
	}
}
