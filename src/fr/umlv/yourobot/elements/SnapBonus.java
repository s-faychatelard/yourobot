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
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import fr.umlv.yourobot.physics.World;

public class SnapBonus extends Bonus {

	private static final String imagePath = "snapBonus.png";
	
	// Bound the random duration generated for each object
	private static final int MINIMUM_DURATION = 5;
	private static final int MAXIMUM_DURATION = 15;
	
	// effective duration of the RobotFake
	private final int duration;
	
	private Date date;
	private long startTime;
	private LinkedList<SnapElement> snapElements;
	
	/**
	 * Private class represents element affected by the snap
	 */
	private static class SnapElement {
		Element element;
		BodyType oldBodyType;
		Joint joint;
	}

	/**
	 * Create a snap bonus
	 * 
	 * @param position of the bonus
	 */
	public SnapBonus(Vec2 position) {
		//Null is test by super
		super(position);
		duration = (new Random()).nextInt(MAXIMUM_DURATION - MINIMUM_DURATION) + MINIMUM_DURATION;
	}

	/**
	 * Return the resource name of the snap bonus
	 */
	@Override
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Start the snap
	 * Create a list of affected elements.
	 * Only elements near the executor robot can be affect (around eigth diagonal of the screen)
	 */
	@Override
	public void execute(final RobotPlayer robot) {
		Objects.requireNonNull(robot);
		date = new Date();
		startTime = date.getTime();
		LinkedList<Element> elements = World.getAllElement();
		snapElements = new LinkedList<>();
		for(final Element element : elements) {
			if(element instanceof EndPoint || element instanceof StartPoint || element instanceof RobotAI || element instanceof Bonus || element instanceof RobotFake) continue;
			//Get the distance from the robot to the element
			int x = (int)robot.getBody().getPosition().x - (int)element.getBody().getPosition().x;
			int y = (int)robot.getBody().getPosition().y - (int)element.getBody().getPosition().y;
			double distance = Math.sqrt(x*x + y*y);
			if(distance>QUARTER_DIAGONAL/2) continue;
			
			SnapElement snapElement = new SnapElement();
			snapElement.element = element;
			RevoluteJointDef rjd = new RevoluteJointDef();
			snapElement.oldBodyType = element.getBody().getType();
			element.getBody().setType(BodyType.DYNAMIC);
			rjd.initialize(robot.getBody(), element.getBody(), new Vec2(100,100));
			snapElement.joint = World.addJoint(rjd);
			snapElements.add(snapElement);
		}
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
		for(SnapElement snapElement : snapElements) {
			World.deleteJoint(snapElement.joint);
			snapElement.element.getBody().setType(snapElement.oldBodyType);
		}
		snapElements.clear();
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
