package fr.umlv.yourobot;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public abstract class Robot implements Element {
	private final static int ROBOT_WIDTH = 44;
	private final static int ROBOT_HEIGTH = 44;
	private final static int INITIAL_SPEED = 50;
	private PolygonShape blockShape;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Body body;
	private static Image image;
	private int life;

	private double direction = 0.;

	public Robot(Vec2 position) {	
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(position.x, position.y);

		blockShape = new PolygonShape();
		blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = blockShape;
		fixtureDef.density = 0.f;
		fixtureDef.friction = 1.f;
		fixtureDef.restitution = 0.f;
		
		life = 100;
	}

	@Override
	public void draw(Graphics2D graphics) {
		Vec2 p = this.body.getPosition();
		//Load only once the picture
		if(image==null) {
			image = Toolkit.getDefaultToolkit().getImage("robot.png");
		}
		//The number 20 is just for the offset to allow picture cut
	    BufferedImage sourceBI = new BufferedImage(ROBOT_WIDTH, ROBOT_HEIGTH, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = (Graphics2D) sourceBI.getGraphics();
	    //Idem than the number 20, it's just to allow picture cut
	    g.drawImage(image, 0, 0, null);
	    AffineTransform at = new AffineTransform();
	    at.scale(1.,1.);
	    //The picture is 20pixels larger than the original
	    at.rotate(this.direction*Math.PI/180, ROBOT_WIDTH/2, ROBOT_HEIGTH/2);
	    BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	    BufferedImage destinationBI = bio.filter(sourceBI, null);
	    graphics.drawImage(destinationBI, (int)p.x, (int)p.y, null);
	}
	
	public void rotate(int rotation) {
		if(this.life<=0) return;
		direction = (direction + rotation)%360;
		if(direction<0) direction = 360+direction;
		Vec2 vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(direction))*INITIAL_SPEED;
		vec.y = (float)Math.sin(Math.toRadians(direction))*INITIAL_SPEED;
		this.body.setLinearVelocity(vec);
	}

	public void rotateLeft() {
		rotate(-20);
	}

	public void rotateRight() {
		rotate(20);
	}
	
	public void jumpTo(Vec2 vec) {
		if(vec == null) {
			this.body.applyForce(new Vec2(0,0), this.getBody().getLocalCenter());
			rotate(0);
			return;
		}
		Vec2 p1 = this.getBody().getPosition();
		Vec2 p2 = vec;
		int x = (int)(p2.x - p1.x);
		int y = -(int)(p2.y - p1.y);
		double hypo = Math.sqrt(x*x + y*y);
		direction = Math.toDegrees(Math.acos(y/hypo));
		if(x<0)
			direction = 360 - direction;
		direction = direction - 90;
		if(direction<0) direction = 360+direction;
		vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(direction))*INITIAL_SPEED*100;
		vec.y = (float)Math.sin(Math.toRadians(direction))*INITIAL_SPEED*100;
		this.body.setLinearVelocity(vec);
	}
	
	public void setLife(int life) {
		this.life = life;
		//System.out.println("new life : " + life);
		if(this.life <= 0) {
			this.body.setLinearVelocity(new Vec2(0,0));
			//System.out.println("dead");
		}
	}

	public int getLife() {
		return this.life;
	}
	
	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public BodyDef getBodyDef() {
		return this.bodyDef;
	}

	@Override
	public FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
}
