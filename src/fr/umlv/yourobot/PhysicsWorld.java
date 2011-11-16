package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class PhysicsWorld {
	private static World world;
	private static LinkedBlockingDeque<Element> elementList;
	boolean matrix[][];
	private Lock lock;




	/**
	 * TODO
	 * Passer le numero du niveau en param pour gérer le niveau de difficulté. 
	 */
	public PhysicsWorld() {
		lock = new ReentrantLock();

		world = new World(new Vec2(0,0), true);

		world.setContactListener(new PhysicsCollision());
		elementList = new LinkedBlockingDeque<>(1000); /** TODO*/

		
		generateWorldBounds();
		generateWalls(0);

		bonusManager();
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
				while(true){
					try {
						Thread.sleep((new Random()).nextInt(5000)+1000);
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
							 final Bonus b = new SnapBonus(v);
							/** TODO : pb de concurrence **/
							addElement(b);
							matrix[(i+r1)%matrix.length][(j+r2)%matrix[0].length] = true;
							i = matrix.length; /**TODO : expliquer en quoi ça permet de sortir des 2 boucles**/
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
		return elementList;
	}

	public Element addElement(Element element) {
		Body elementBody = null;

		while (!lock.tryLock()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			//createBody auto lock
			elementBody = world.createBody(element.getBodyDef());
			if(element.getFixtureDef() != null)
				elementBody.createFixture(element.getFixtureDef());
			elementBody.setType(element.getBodyDef().type);		
			element.setBody(elementBody);
			elementBody.setUserData(element);
			elementList.addFirst(element);
		} finally {
			lock.unlock();
		}

		return element;
	}

	public void raycast(RayCastCallbackRobotIA raycastCallback, Vec2 point1, Vec2 point2) {
		world.raycast(raycastCallback, point1, point2);
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
