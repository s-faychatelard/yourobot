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

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.World;

public class RobotFakeBonus extends Bonus {
	
	private static final String imagePath = "robotFakeBonus.png";
	
	// Bound the random duration generated for each object
	private static final int MINIMUM_DURATION = 2;
	private static final int MAXIMUM_DURATION = 6;
	
	// effective duration of the RobotFake
	private final int duration;
	
	private Date date;
	private long startTime;
	private RobotFake currentRobot;

	/**
	 * Create a fake robot bonus
	 * 
	 * @param position of the fake robot bonus
	 */
	public RobotFakeBonus(Vec2 position) {
		//Null is test by super
		super(position);
		duration = (new Random()).nextInt(MAXIMUM_DURATION - MINIMUM_DURATION) + MINIMUM_DURATION;
	}
	
	/**
	 * Return the resource name of the fake robot bonus
	 */
	@Override
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Execute the bonus
	 * 
	 * @param robot correspond to player who launch the fake robot
	 */
	@Override
	public void execute(final RobotPlayer robot) {
		Objects.requireNonNull(robot);
		date = new Date();
		startTime = date.getTime();
		currentRobot = new RobotFake(new Vec2(robot.getBody().getPosition().x-25, robot.getBody().getPosition().y-25));
		World.addRobotFake(currentRobot);
	}
	
	/**
	 * Update the bonus state
	 * 
	 * @return the bonus if it continue to work or null if it terminate
	 */
	@Override
	public Bonus update() {
		date = new Date();
		long time = date.getTime();
		if(time<startTime+(duration*1000)) return this;
		World.removeRobotFake(currentRobot);
		return null;
	}

	/**
	 * Get the duration of the bonus
	 */
	@Override
	public int getDuration() {
		return this.duration;
	}
}
