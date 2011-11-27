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
	private final int executionTime;
	private Date date;
	private long startTime;
	private RobotFake currentRobot;

	public RobotFakeBonus(Vec2 position) {
		//Null is test by super
		super(position);
		Random rand = new Random();
		executionTime = rand.nextInt(4000)+2000;
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public void execute(final RobotPlayer robot) {
		Objects.requireNonNull(robot);
		date = new Date();
		startTime = date.getTime();
		currentRobot = new RobotFake(new Vec2(robot.getBody().getPosition().x-25, robot.getBody().getPosition().y-25));
		World.addRobotFake(currentRobot);
	}
	
	@Override
	public Bonus update() {
		date = new Date();
		long time = date.getTime();
		if(time<startTime+executionTime) return this;
		World.removeLeurre(currentRobot);
		return null;
	}
}
