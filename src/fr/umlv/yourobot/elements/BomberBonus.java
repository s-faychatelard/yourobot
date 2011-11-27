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

import fr.umlv.yourobot.physics.World;

public class BomberBonus extends Bonus {
	private static final int breathDuration = 2500;
	
	private Date date;
	private long startTime;
	private final WallType wallType;
	private LinkedList<BomberElement> bomberElements;

	private enum WallType {
		ICE("bomberIceBonus.png"), 
		WOOD("bomberWoodBonus.png"), 
		STONE("bomberStoneBonus.png");
		
		private String imagePath;
		private WallType(String imagePath) {
			this.imagePath = imagePath;
		}
		
	}
	
	private static class BomberElement {
		Element element;
		BodyType oldBodyType;
	}

	public BomberBonus(Vec2 position) {
		//Null is test by super
		super(position);
		Random rand = new Random();
		switch(rand.nextInt(4)) {
		case 0:
			wallType=WallType.ICE;
			break;
		case 1:
			wallType=WallType.WOOD;
			break;
		default:
			wallType=WallType.STONE;
			break;
		}
	}

	@Override
	public String getImagePath() {
		return wallType.imagePath;
	}

	@Override
	public void execute(RobotPlayer robot) {
		Objects.requireNonNull(robot);
		LinkedList<Element> elements = World.getAllElement();
		bomberElements = new LinkedList<>();
		date = new Date();
		startTime = date.getTime();
		for(Element element : elements) {
			if((element instanceof RobotPlayer) || (element instanceof StartPoint) || (element instanceof EndPoint)) continue;
			//Get the distance from the robot to the element
			Vec2 pos = new Vec2(robot.getBody().getPosition());
			int x = (int)robot.getBody().getPosition().x - (int)element.getBody().getPosition().x;
			int y = (int)robot.getBody().getPosition().y - (int)element.getBody().getPosition().y;
			double distance = Math.sqrt(x*x + y*y);
			if(distance>QUARTER_DIAGONAL) continue;

			//Calculate the coefficient force (more if the element is near the robot)
			double coeffForce = QUARTER_DIAGONAL/distance;

			Vec2 force = pos.sub(element.getBody().getPosition()).negate();
			int wallCoeff=5000;
			if((element instanceof IceWall) && wallType == WallType.ICE)
				wallCoeff=10000;
			else if((element instanceof WoodWall) && wallType == WallType.WOOD)
				wallCoeff=10000;
			else if((element instanceof StoneWall) && wallType == WallType.STONE)
				wallCoeff=10000;
			double coeffWallForce = wallCoeff*coeffForce;

			BomberElement bomberElement = new BomberElement();
			bomberElement.element = element;
			bomberElement.oldBodyType = element.getBody().getType();
			element.getBody().setType(BodyType.DYNAMIC);	
			element.getBody().setLinearDamping(.8f);
			element.getBody().setAwake(true);
			element.getBody().applyForce(new Vec2((int)(force.x*coeffWallForce),(int)(force.y*coeffWallForce)), element.getBody().getPosition());
			bomberElements.add(bomberElement);
		}
	}
	
	@Override
	public Bonus update() {
		date = new Date();
		long time = date.getTime();
		if(time<startTime+breathDuration) return this;
		for(BomberElement bomberElement : bomberElements)
			bomberElement.element.getBody().setType(bomberElement.oldBodyType);
		bomberElements.clear();
		return null;
	}

	@Override
	public int getDuration() {
		return 0;
	}
}
