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
import fr.umlv.yourobot.elements.FakeRobot;
import fr.umlv.yourobot.elements.FakeRobotBonus;
import fr.umlv.yourobot.elements.IceWall;
import fr.umlv.yourobot.elements.Robot;
import fr.umlv.yourobot.elements.RobotIA;
import fr.umlv.yourobot.elements.RobotPlayer;
import fr.umlv.yourobot.elements.SnapBonus;
import fr.umlv.yourobot.elements.StartPoint;
import fr.umlv.yourobot.elements.StoneWall;
import fr.umlv.yourobot.elements.Wall;
import fr.umlv.yourobot.elements.WoodWall;
import fr.umlv.zen.KeyboardKey;

public class World {
	private static org.jbox2d.dynamics.World world;
	//LinkedBlockingDeque offer addFirst, addLast methods
	private static final LinkedList<Element> elementsList = new LinkedList<>();
	private static final LinkedList<Robot> detectabelRobots = new LinkedList<>();
	private static RobotPlayer []robotsPlayer;
	private final ArrayList<RobotIA> robotIA = new ArrayList<>();
	private boolean matrix[][];

	public World(int numberOfPlayer, int level) {
		if(numberOfPlayer<=0 || numberOfPlayer>2) throw new IllegalArgumentException("Number of player need to at least 1 and not more than 2");

		//world must be create before adding element
		world = new org.jbox2d.dynamics.World(new Vec2(0,0), true);
		world.setContinuousPhysics(true);
		world.setContactListener(new Collision());

		if(elementsList!=null) elementsList.clear();
		if(detectabelRobots!=null) detectabelRobots.clear();

		//Generate global world
		matrix =  new boolean[Main.WIDTH / Wall.WALL_SIZE][Main.HEIGHT / Wall.WALL_SIZE];
		generateWorldBounds();
		generateWallsAndBonuses(level, matrix);

		//Generate Player
		Random rand = new Random();
		int x,y;
		robotsPlayer = new RobotPlayer[numberOfPlayer];
		for(int i=0;i<numberOfPlayer;i++) {
			//Try to start in the top left
			x=rand.nextInt(150);
			y=rand.nextInt(150);
			robotsPlayer[i] = (RobotPlayer)this.addElement(new RobotPlayer(new Vec2(x, y)));
			detectabelRobots.add(robotsPlayer[i]);
			//Add it start circle
			this.addElement(new StartPoint(new Vec2(x, y)));
		}
		//Add the end
		this.addElement(new EndPoint(new Vec2(Main.WIDTH-50, Main.HEIGHT-50)));

		//Generate IA
		int numberOfIA = level%5;
		for(int i=0;i<numberOfIA;i++) {
			x=rand.nextInt(Main.WIDTH);
			y=rand.nextInt(Main.HEIGHT);
			RobotIA r = ((RobotIA)this.addElement(new RobotIA(new Vec2(x, y))));
			robotIA.add(r);
		}
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
		for(RobotIA ia : robotIA)
			ia.update();
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

	public static void addLeurre(FakeRobot fr) {
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

	public static void removeLeurre(FakeRobot fr) {
		world.destroyBody(fr.getBody());
		detectabelRobots.remove(fr);
		elementsList.remove(fr);
	}

	public static LinkedList<Robot> getDetectableRobot() {
		return detectabelRobots;
	}

	public void raycast(RayCastCallbackRobotIA raycastCallback, Vec2 point1, Vec2 point2) {
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

		int numberOfWalls = Main.WIDTH * Main.HEIGHT / (Wall.WALL_SIZE * Wall.WALL_SIZE) / 20;
		numberOfWalls = Math.round (numberOfWalls * (1 + (difficulty/5)));
		
		int numberOfBonus = Main.WIDTH * Main.HEIGHT / (Wall.WALL_SIZE * Wall.WALL_SIZE) / 40;
		numberOfBonus = Math.round (numberOfBonus * (1 + (difficulty/5)));

		Random rand = new Random();
		for (int i=0; i < matrix.length - 1; i++) {
			for (int j=0; j< matrix[0].length - 1; j++) {
				if(rand.nextInt(matrix[0].length * matrix.length) < numberOfWalls) {
					Vec2 v = new Vec2((i * Wall.WALL_SIZE), (j * Wall.WALL_SIZE));
					int r = rand.nextInt(2);
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
					Vec2 v = new Vec2((i * Wall.WALL_SIZE), (j * Wall.WALL_SIZE));
					int r = rand.nextInt(2);
					switch(r) {
					case 0 :
						this.addElement(new FakeRobotBonus(v));
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
