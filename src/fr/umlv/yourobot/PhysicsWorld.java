package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

import fr.umlv.zen.KeyboardKey;


public class PhysicsWorld {
	private static final int MAX_ELEMENT=200;
	private static World world;
	//LinkedBlockingDeque offer concurrent list and addFirst, addLast methods
	private static LinkedBlockingDeque<Element> elementList;
	private static LinkedBlockingDeque<Robot> robots;
	boolean matrix[][];
	private static Lock lock;
	static RobotPlayer []robot;


	//TODO add level difficulty
	public PhysicsWorld(int numberOfPlayer) {
		if(numberOfPlayer<=0 || numberOfPlayer>2) throw new IllegalArgumentException("Number of player need to at least 1 and not more than 2");
		
		if(lock==null) {
			lock = new ReentrantLock();
		}

		//world must be create before adding element
		world = new World(new Vec2(0,0), true);
		world.setContinuousPhysics(true);
		world.setContactListener(new PhysicsCollision());
		if(elementList == null) {
			elementList = new LinkedBlockingDeque<>(MAX_ELEMENT);
		}
		if(robots==null) {
			robots = new LinkedBlockingDeque<>(MAX_ELEMENT);
		}

		//Need to create robot before IA to add detection for each RobotPlayer TODO change static position when generate world
		generateWorldBounds();
		generateWalls(5);

		//Generate Player
		Random rand = new Random();
		int x,y;
		robot = new RobotPlayer[numberOfPlayer];
		for(int i=0;i<numberOfPlayer;i++) {
			//Try to start in the top left
			x=rand.nextInt(150);
			y=rand.nextInt(150);
			robot[i] = (RobotPlayer)this.addElement(new RobotPlayer(new Vec2(x, y)));
			robots.add(robot[i]);
			//Add it start circle
			this.addElement(new StartPoint(new Vec2(x, y)));
		}
		//Add the end
		this.addElement(new EndPoint(new Vec2(Main.WIDTH-50, Main.HEIGHT-50)));

		//Generate IA TODO calculate number of IA with the difficulty level
		/*for(int i=0;i<4;i++) {
			x=rand.nextInt(Main.WIDTH);
			y=rand.nextInt(Main.HEIGHT);
			RobotIA r = ((RobotIA)this.addElement(new RobotIA(new Vec2(x, y))));
			r.start();
		}*/

		bonusManager();
	}

	public void setKey(KeyboardKey key) {
		switch(key) {
		case UP:
			robot[0].throttle();
			break;
		case M:
			robot[0].useBonus();
			break;
		case LEFT:
			robot[0].rotateLeft();
			break;
		case RIGHT:
			robot[0].rotateRight();
			break;
		case Z:
			if(robot.length==1) break;
			robot[1].throttle();
			break;
		case A:
			if(robot.length==1) break;
			robot[1].useBonus();
			break;
		case Q:
			if(robot.length==1) break;
			robot[1].rotateLeft();
			break;
		case D:
			if(robot.length==1) break;
			robot[1].rotateRight();
			break;
		}
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

	private void bonusManager() {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				while(true){
					try {
						Thread.sleep((new Random()).nextInt(1000)+1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					int r1 = (new Random()).nextInt(matrix.length);
					int r2 = (new Random()).nextInt(matrix[0].length);

					// note : it's impossible to fill all the matrix : there is at less a player that will take a bonus
					for (int i=0; i<matrix.length; i++) {
						for (int j=0; j<matrix[0].length; j++) {
							if(matrix[(i+r1)%matrix.length][(j+r2)%matrix[0].length]) {
								continue;
							}
							Vec2 v = new Vec2((float)((i+r1)%matrix.length) * Wall.WALL_WIDTH, (float)((j+r2)%matrix[0].length) * Wall.WALL_HEIGTH);
							final Bonus b;
							int r = rand.nextInt(3);
							switch(r) {
							case 0 :
								b = new BomberBonus(v);
								break;
							case 1 :
								b = new SnapBonus(v);
								break;	
							default :
								b = new FakeRobotBonus(v);
								break;
							}
							addElement(b);
							matrix[(i+r1)%matrix.length][(j+r2)%matrix[0].length] = true;
							i = matrix.length; // expliquer en quoi a permet de sortir des 2 boucles
							break;

						}
					}
				}

			}
		});

		t.start();
	}

	/**
	 * @param difficulty
	 */
	private void generateWalls(int difficulty){
		if(difficulty<0) throw new IllegalArgumentException("difficulty cannot be lower than 0");
		int cellWidth = Main.WIDTH / Wall.WALL_WIDTH;
		int cellHeight = Main.HEIGHT / Wall.WALL_HEIGTH;
		boolean matrix[][] =  new boolean[cellWidth][cellHeight];

		int numberOfWalls = Main.WIDTH * Main.HEIGHT / (Wall.WALL_WIDTH * Wall.WALL_HEIGTH) / 20;
		numberOfWalls = Math.round (numberOfWalls * (1 + (difficulty/10)));

		Random rand = new Random();
		for (int i=0; i < matrix.length - 1; i++) {
			for (int j=0; j< matrix[0].length - 1; j++) {
				if(rand.nextInt(matrix[0].length * matrix.length) < numberOfWalls) {

					int r = rand.nextInt(3);
					Vec2 v = new Vec2((i * Wall.WALL_WIDTH), (j * Wall.WALL_HEIGTH));
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

				}
			}
		}
		this.matrix = matrix;

	}

	public static LinkedBlockingDeque<Element> getAllElement() {
		while (!lock.tryLock()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			return elementList;
		} finally {
			lock.unlock();
		}
	}

	public Element addElement(Element element) {
		Objects.requireNonNull(element);
		Body elementBody = null;
		while (!lock.tryLock()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			//createBody is already auto lock
			elementBody = world.createBody(element.getBodyDef());
			if(element.getFixtureDef() != null)
				elementBody.createFixture(element.getFixtureDef());
			elementBody.setType(element.getBodyDef().type);		
			element.setBody(elementBody);
			elementBody.setUserData(element);
			if(element.getClass().getSuperclass() == Robot.class || element.getClass().getSuperclass() == Wall.class)
				elementList.addLast(element);
			else
				elementList.addFirst(element);
		} finally {
			lock.unlock();
		}

		return element;
	}

	public static void addRobotToDetectableList(Robot robot) {
		//Cannot call addElement because we are in a static method
		//createBody is already auto lock
		Body elementBody = null;
		elementBody = world.createBody(robot.getBodyDef());
		if(robot.getFixtureDef() != null)
			elementBody.createFixture(robot.getFixtureDef());
		elementBody.setType(robot.getBodyDef().type);		
		robot.setBody(elementBody);
		elementBody.setUserData(robot);
		if(robot.getClass().getSuperclass() == Robot.class || robot.getClass().getSuperclass() == Wall.class)
			elementList.addLast(robot);
		else
			elementList.addFirst(robot);
		robots.addFirst(robot);
	}

	public static void addLeurre(FakeRobot fr) {
		//Is insert in top of the queue so he is the first robot to be detect
		addRobotToDetectableList(fr);
	}
	
	public static void removeBody(Element element) {
		world.destroyBody(element.getBody());
		while (!lock.tryLock()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			elementList.remove(element);
		} finally {
			lock.unlock();
		}
	}

	public static void removeLeurre(FakeRobot fr) {
		world.destroyBody(fr.getBody());
		robots.remove(fr);
		while (!lock.tryLock()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			elementList.remove(fr);
		} finally {
			lock.unlock();
		}
	}

	public static LinkedBlockingDeque<Robot> getDetectableRobot() {
		return robots;
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

	public void updateWorld() {
		while(!lock.tryLock()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			world.step(1/30f, 15, 8);
		} finally {
			lock.unlock();
		}
	}

	public void render(Graphics2D graphics) {
		for(Element e : elementList)
			e.draw(graphics);
	}
}
