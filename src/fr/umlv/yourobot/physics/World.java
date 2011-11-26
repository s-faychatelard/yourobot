package fr.umlv.yourobot.physics;

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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.elements.BomberBonus;
import fr.umlv.yourobot.elements.Bonus;
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
	private static final int MAX_ELEMENT=200;
	private static final org.jbox2d.dynamics.World world = new org.jbox2d.dynamics.World(new Vec2(0,0), true);
	//LinkedBlockingDeque offer concurrent list and addFirst, addLast methods
	private static final LinkedBlockingDeque<Element> elementList = new LinkedBlockingDeque<>(MAX_ELEMENT);
	private static final LinkedBlockingDeque<Robot> robots = new LinkedBlockingDeque<>(MAX_ELEMENT);
	private boolean matrix[][];
	private final static Lock lock = new ReentrantLock();
	//private static 
	private static RobotPlayer []fr;


	//TODO add level difficulty
	public World(int numberOfPlayer) {
		if(numberOfPlayer<=0 || numberOfPlayer>2) throw new IllegalArgumentException("Number of player need to at least 1 and not more than 2");

		//world must be create before adding element
		//world.setContinuousPhysics(true);
		world.setContactListener(new Collision());

		//Need to create robot before IA to add detection for each RobotPlayer TODO change static position when generate world
		generateWorldBounds();
		generateWalls(5);

		//Generate Player
		Random rand = new Random();
		int x,y;
		fr = new RobotPlayer[numberOfPlayer];
		for(int i=0;i<numberOfPlayer;i++) {
			//Try to start in the top left
			x=rand.nextInt(150);
			y=rand.nextInt(150);
			fr[i] = (RobotPlayer)this.addElement(new RobotPlayer(new Vec2(x, y)));
			robots.add(fr[i]);
			//Add it start circle
			this.addElement(new StartPoint(new Vec2(x, y)));
		}
		//Add the end
		this.addElement(new EndPoint(new Vec2(Main.WIDTH-50, Main.HEIGHT-50)));

		//Generate IA TODO calculate number of IA with the difficulty level
		for(int i=0;i<4;i++) {
			x=rand.nextInt(Main.WIDTH);
			y=rand.nextInt(Main.HEIGHT);
			RobotIA r = ((RobotIA)this.addElement(new RobotIA(new Vec2(x, y))));
			r.start();
		}

		bonusManager();
	}

	public void setKey(KeyboardKey key) {
		switch(key) {
		case UP:
			fr[0].throttle();
			break;
		case M:
			fr[0].useBonus();
			break;
		case LEFT:
			fr[0].rotateLeft();
			break;
		case RIGHT:
			fr[0].rotateRight();
			break;
		case Z:
			if(fr.length==1) break;
			fr[1].throttle();
			break;
		case A:
			if(fr.length==1) break;
			fr[1].useBonus();
			break;
		case Q:
			if(fr.length==1) break;
			fr[1].rotateLeft();
			break;
		case D:
			if(fr.length==1) break;
			fr[1].rotateRight();
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
							Vec2 v = new Vec2((float)((i+r1)%matrix.length) * Wall.WALL_SIZE, (float)((j+r2)%matrix[0].length) * Wall.WALL_SIZE);
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
		int cellWidth = Main.WIDTH / Wall.WALL_SIZE;
		int cellHeight = Main.HEIGHT / Wall.WALL_SIZE;
		boolean matrix[][] =  new boolean[cellWidth][cellHeight];

		int numberOfWalls = Main.WIDTH * Main.HEIGHT / (Wall.WALL_SIZE * Wall.WALL_SIZE) / 20;
		numberOfWalls = Math.round (numberOfWalls * (1 + (difficulty/10)));

		Random rand = new Random();
		for (int i=0; i < matrix.length - 1; i++) {
			for (int j=0; j< matrix[0].length - 1; j++) {
				if(rand.nextInt(matrix[0].length * matrix.length) < numberOfWalls) {

					int r = rand.nextInt(3);
					Vec2 v = new Vec2((i * Wall.WALL_SIZE), (j * Wall.WALL_SIZE));
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
		lock.lock();
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

	public static void addLeurre(FakeRobot fr) {
		Objects.requireNonNull(fr);
		//Is insert in top of the queue so he is the first robot to be detect
		//Cannot call addElement because we are in a static method
		//createBody is already auto loc
		lock.lock();
		try {
			Body elementBody = world.createBody(fr.getBodyDef());
			elementBody.createFixture(fr.getFixtureDef());
			elementBody.setType(fr.getBodyDef().type);		
			fr.setBody(elementBody);
			elementBody.setUserData(fr);
			if(fr.getClass().getSuperclass() == Robot.class || fr.getClass().getSuperclass() == Wall.class)
				elementList.addLast(fr);
			else
				elementList.addFirst(fr);
			robots.addFirst(fr);
		} finally {
			lock.unlock();
		}
	}

	public static void removeBody(Element element) {
		lock.lock();
		try {
			world.destroyBody(element.getBody());
			elementList.remove(element);
		} finally {
			lock.unlock();
		}
	}

	public static void removeLeurre(FakeRobot fr) {
		lock.lock();
		try {
			world.destroyBody(fr.getBody());
			robots.remove(fr);
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
		lock.lock();
		try {
			world.raycast(raycastCallback, point1, point2);
		} finally {
			lock.unlock();
		}
	}

	public static Joint addJoint(JointDef joint) {
		Objects.requireNonNull(joint);
		lock.lock();
		try {
			return world.createJoint(joint);
		} finally {
			lock.unlock();
		}
	}

	public static void deleteJoint(Joint joint) {
		Objects.requireNonNull(joint);
		lock.lock();
		try {
			world.destroyJoint(joint);
		} finally {
			lock.unlock();
		}
	}

	public void updateWorld() {
		lock.lock();
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
