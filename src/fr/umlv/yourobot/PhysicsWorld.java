package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	private static World world;
	private static ArrayList<Element> elementList;
	Body []limits;

	/**
	 * TODO
	 * Passer le numero du niveau en param pour gérer le niveau de difficulté. 
	 */
	public PhysicsWorld() {
		world = new World(new Vec2(0,0), true);
		world.setContactListener(new PhysicsCollision());
		elementList = new ArrayList<>();

		//TODO transform limits has Wall but keep this position and size
		limits = new Body[4];
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
		limits[0].setType(BodyType.STATIC);
		
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
		limits[1].setType(BodyType.STATIC);
		
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
		limits[2].setType(BodyType.STATIC);
		
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
		limits[3].setType(BodyType.STATIC);
		
		generateWalls(0);
	}
	
/**
	 * @param difficulty
	 */
	private void generateWalls(int difficulty){
		
		int cellWidth = Main.WIDTH / Wall.WALL_WIDTH;
		int cellHeight = Main.HEIGHT / Wall.WALL_HEIGTH;
		boolean matrice[][] =  new boolean[cellWidth][cellHeight];

		int numberOfWalls = Main.WIDTH * Main.HEIGHT / (Wall.WALL_WIDTH * Wall.WALL_HEIGTH) / 30;
		numberOfWalls = Math.round (numberOfWalls * (1 + (difficulty/10)));
		
		System.out.println(numberOfWalls);
		System.out.println(matrice[0].length * matrice.length);
		
		Random rand = new Random();
		int cpt = 0;
		for (int i=0; i < matrice.length - 1; i++) {
			for (int j=0; j< matrice[0].length - 1; j++) {
				if(rand.nextInt(matrice[0].length * matrice.length) < numberOfWalls) {
					this.addElement(new IceWall(new Vec2((i * Wall.WALL_WIDTH), (j * Wall.WALL_HEIGTH))));
					matrice[i][j] = true;
					cpt++;
					
				}
			}
		}
		System.out.println(cpt);
		
		
		
	/*	for (int i=0; i < matrice.length - 1; i++) {
			for (int j=0; j< matrice[0].length - 1; j++) {
			
					System.out.println(matrice[i][j]);
					
			
			}
		}*/
	}
	
	public static ArrayList<Element> getAllElement() {
		return elementList;
	}
	
	public Element addElement(Element element) {
		Objects.requireNonNull(element);
		Body elementBody = world.createBody(element.getBodyDef());
		elementBody.createFixture(element.getFixtureDef());
		elementBody.setType(element.getBodyDef().type);		
		element.setBody(elementBody);
		elementList.add(element);
		return element;
	}
	
	public static void addRaycast(RayCastCallback callback, Vec2 point1, Vec2 point2) {
		world.raycast(callback, point1, point2);
	}

	public void render(Graphics2D graphics) {
		world.step(1/60f, 15, 8);
		graphics.setColor(Color.BLACK);
		for(Element e : elementList) {
			e.draw(graphics);
		}
	}
}
