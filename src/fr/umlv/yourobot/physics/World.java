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

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.elements.BomberBonus;
import fr.umlv.yourobot.elements.Element;
import fr.umlv.yourobot.elements.EndPoint;
import fr.umlv.yourobot.elements.RobotFake;
import fr.umlv.yourobot.elements.RobotFakeBonus;
import fr.umlv.yourobot.elements.IceWall;
import fr.umlv.yourobot.elements.Robot;
import fr.umlv.yourobot.elements.RobotAI;
import fr.umlv.yourobot.elements.RobotPlayer;
import fr.umlv.yourobot.elements.SnapBonus;
import fr.umlv.yourobot.elements.StartPoint;
import fr.umlv.yourobot.elements.StoneWall;
import fr.umlv.yourobot.elements.Wall;
import fr.umlv.yourobot.elements.WoodWall;
import fr.umlv.zen.KeyboardKey;

public class World {
	private static final int CELL_SIZE = Wall.SIZE;
	
	// Size (number of cells) of the top left and right bottom corners without walls/bonuses/AIs
	private static final int SAFE_AREA_SIZE = 3;
	
	private static org.jbox2d.dynamics.World world;
	private static final LinkedList<Element> elementsList = new LinkedList<>();
	private static final LinkedList<Robot> detectabelRobots = new LinkedList<>();
	private static RobotPlayer []robotsPlayer;
	private final ArrayList<RobotAI> robotsIA = new ArrayList<>();
	private boolean matrix[][];

	public World(int numberOfPlayers, int level) {
		if(numberOfPlayers<=0 || numberOfPlayers>2) throw new IllegalArgumentException("Number of player need to at least 1 and not more than 2");

		//world must be create before adding element
		world = new org.jbox2d.dynamics.World(new Vec2(0,0), true);
		world.setContinuousPhysics(true);
		world.setContactListener(new Collision());

		if(elementsList!=null) elementsList.clear();
		if(detectabelRobots!=null) detectabelRobots.clear();

		//Generate global world
		matrix =  new boolean[Main.WIDTH / CELL_SIZE][Main.HEIGHT / CELL_SIZE];
		
		// Book spaces on the top left and bottom right corners (start and end points) 
		for (int i=0; i < SAFE_AREA_SIZE; i++)
			for (int j=0; j< SAFE_AREA_SIZE; j++)
				matrix[i][j] = true;
		for (int i=matrix.length - 1 - SAFE_AREA_SIZE; i < matrix.length; i++) 
			for (int j=matrix[0].length - 1 - SAFE_AREA_SIZE; j< matrix[0].length; j++)
				matrix[i][j] = true;
		
		generateWorldBounds();
		generateWallsAndBonuses(level, matrix);
		generatePlayersAndGates(numberOfPlayers);
		generateAI(level);
	}

	private void generateAI(int level) {
		Random rand = new Random();
		int numberOfAI = level % 5;
		for(int i=0; i<numberOfAI; i++) {
			int x = rand.nextInt(matrix.length - 1 - SAFE_AREA_SIZE) + SAFE_AREA_SIZE; // TODO - 4 = -1 - 3 (3 for booked space in start area)
			int y = rand.nextInt(matrix[0].length - 1 - SAFE_AREA_SIZE) + SAFE_AREA_SIZE;
			int j = 0, k = 0;
			
			// if the cell is busy, we try the next
			while(matrix[(x+j)%matrix.length][(y+k)%matrix[y].length] && // busy cell 
					j< matrix.length && k < matrix[0].length){ // full matrix
				j++;
				k++;
			}
			matrix[(x+j)%matrix.length][(y+k)%matrix[0].length] = true;
			RobotAI r = ((RobotAI)this.addElement(new RobotAI(new Vec2(((x+j)%matrix.length)*CELL_SIZE, ((y+k)%matrix[0].length)*CELL_SIZE))));
			robotsIA.add(r);
		}
	}

	private void generatePlayersAndGates(int numberOfPlayers) {
		//Generate Players
		Random rand = new Random();
		robotsPlayer = new RobotPlayer[numberOfPlayers];
		for(int i=0;i<numberOfPlayers;i++) {
			//Try to start in the top left
			int x = rand.nextInt(2) * CELL_SIZE;
			int y = rand.nextInt(2) * CELL_SIZE;
			robotsPlayer[i] = (RobotPlayer)this.addElement(new RobotPlayer(new Vec2(x, y)));
			detectabelRobots.add(robotsPlayer[i]);
			
			//Add it start circle
			this.addElement(new StartPoint(new Vec2(x, y)));
		}
		
		//Add the end
		this.addElement(new EndPoint(new Vec2((matrix.length-1)*CELL_SIZE, (matrix[0].length-1)*CELL_SIZE)));
	}

	public void setKey(KeyboardKey key) {
		switch(key) {
		case UP:
			robotsPlayer[0].throttle();
			break;
		case M:
			robotsPlayer[0].useBonus();
			break;
		case LEFT:
			robotsPlayer[0].rotateLeft();
			break;
		case RIGHT:
			robotsPlayer[0].rotateRight();
			break;
		case Z:
			if(robotsPlayer.length==1) break;
			robotsPlayer[1].throttle();
			break;
		case A:
			if(robotsPlayer.length==1) break;
			robotsPlayer[1].useBonus();
			break;
		case Q:
			if(robotsPlayer.length==1) break;
			robotsPlayer[1].rotateLeft();
			break;
		case D:
			if(robotsPlayer.length==1) break;
			robotsPlayer[1].rotateRight();
			break;
		}
	}

	public void updateWorld() {
		for(RobotAI ia : robotsIA)
			ia.update();
		for(RobotPlayer rp : robotsPlayer)
			rp.update();
		world.step(1/30f, 15, 8);
	}

	public void render(Graphics2D graphics) {
		for(Element e : elementsList)
			e.draw(graphics);
	}
	
	public static void checkRobotsLife() {
		int counter=0;
		for(RobotPlayer rp : robotsPlayer)
			if(rp.getLife()<=0) counter++;

		if(counter==robotsPlayer.length) {
			Main.Lose();
		}
	}

	public static LinkedList<Element> getAllElement() {
		return elementsList;
	}

	public Element addElement(Element element) {
		Objects.requireNonNull(element);
		//createBody is already auto lock
		Body elementBody = world.createBody(element.getBodyDef());
		if(element.getFixtureDef() != null)
			elementBody.createFixture(element.getFixtureDef());
		elementBody.setType(element.getBodyDef().type);		
		element.setBody(elementBody);
		elementBody.setUserData(element);
		if(element.getClass().getSuperclass() == Robot.class || element.getClass().getSuperclass() == Wall.class)
			elementsList.addLast(element);
		else
			elementsList.addFirst(element);

		return element;
	}

	public static void addRobotFake(RobotFake fr) {
		Objects.requireNonNull(fr);
		//Is insert in top of the queue so he is the first robot to be detect
		//Cannot call addElement because we are in a static method
		//createBody is already auto loc
		Body elementBody = world.createBody(fr.getBodyDef());
		elementBody.createFixture(fr.getFixtureDef());
		elementBody.setType(fr.getBodyDef().type);		
		fr.setBody(elementBody);
		elementBody.setUserData(fr);
		elementsList.addFirst(fr);
		detectabelRobots.addFirst(fr);
	}

	public static void removeBody(Element element) {
		world.destroyBody(element.getBody());
		elementsList.remove(element);
	}

	public static void removeLeurre(RobotFake fr) {
		world.destroyBody(fr.getBody());
		detectabelRobots.remove(fr);
		elementsList.remove(fr);
	}

	public static LinkedList<Robot> getDetectableRobot() {
		return detectabelRobots;
	}

	public void raycast(RayCastCallbackRobotAI raycastCallback, Vec2 point1, Vec2 point2) {
		Objects.requireNonNull(raycastCallback);
		Objects.requireNonNull(point1);
		Objects.requireNonNull(point2);
		world.raycast(raycastCallback, point1, point2);
	}

	public static Joint addJoint(JointDef joint) {
		Objects.requireNonNull(joint);
		return world.createJoint(joint);
	}

	public static void deleteJoint(Joint joint) {
		Objects.requireNonNull(joint);
		world.destroyJoint(joint);
	}

	private void generateWorldBounds() {
		// Top border
		Body [] limits = new Body[4];
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(0, -15);
		PolygonShape blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH, 15/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[0] = world.createBody(bodyDef);
		limits[0].createFixture(fixtureDef);

		// left border
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(-15, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(15/2, Main.HEIGHT);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[1] = world.createBody(bodyDef);
		limits[1].createFixture(fixtureDef);

		//bottom
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(0, Main.HEIGHT);
		blockShape = new PolygonShape();
		blockShape.setAsBox(Main.WIDTH, 15/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[2] = world.createBody(bodyDef);
		limits[2].createFixture(fixtureDef);

		// right
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(Main.WIDTH, 0);
		blockShape = new PolygonShape();
		blockShape.setAsBox(15/2, Main.HEIGHT);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 1.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = .1f;
		limits[3] = world.createBody(bodyDef);
		limits[3].createFixture(fixtureDef);
	}

	private void generateWallsAndBonuses(int difficulty, boolean matrix[][]){
		if(difficulty<0) throw new IllegalArgumentException("difficulty cannot be lower than 0");

		int numberOfWalls = Main.WIDTH * Main.HEIGHT / (CELL_SIZE * CELL_SIZE) / 20;
		numberOfWalls = Math.round (numberOfWalls * (1 + (difficulty/5)));
		
		int numberOfBonus = Main.WIDTH * Main.HEIGHT / (CELL_SIZE * CELL_SIZE) / 30;
		numberOfBonus = Math.round (numberOfBonus * (1 + (difficulty/5)));

		Random rand = new Random();
		
		
		for (int i=0; i < matrix.length; i++) {
			for (int j=0; j< matrix[0].length; j++) {
				
				if (!matrix[i][j]) { // do not write on busy spaces
					if(rand.nextInt(matrix[0].length * matrix.length) < numberOfWalls) {
						Vec2 v = new Vec2((i * CELL_SIZE), (j * CELL_SIZE));
						int r = rand.nextInt(3);
						switch(r) {
						case 0 :
							this.addElement(new IceWall(v));
							break;
						case 1 :
							this.addElement(new WoodWall(v));
							break;	
						default :
							this.addElement(new StoneWall(v));
							break;
						}
						matrix[i][j] = true;					
					} else if(rand.nextInt(matrix[0].length * matrix.length) < numberOfBonus) {
						Vec2 v = new Vec2((i * CELL_SIZE), (j * CELL_SIZE));
						int r = rand.nextInt(3);
						switch(r) {
						case 0 :
							this.addElement(new RobotFakeBonus(v));
							break;
						case 1 :
							this.addElement(new BomberBonus(v));
							break;	
						default :
							this.addElement(new SnapBonus(v));
							break;
						}
						matrix[i][j] = true;					
					}
				}
			}
		}
	}
}