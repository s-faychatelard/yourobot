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
	private final static int INITIAL_SPEED = 10;
	private PolygonShape blockShape;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private Body body;
	private static Image image;

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

	public void rotateLeft() {
		//TODO get direction from this.body.getVelocity() ???
		direction = (direction - 90)%360;
		if(direction<0) direction = 360+direction;
		Vec2 vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(direction))*INITIAL_SPEED;
		vec.y = (float)Math.sin(Math.toRadians(direction))*INITIAL_SPEED;
		this.body.setLinearVelocity(vec);
		
		
		//TODO need to check dimension of the PolygonShape because I think something is wrong collision are a little bit weird sometimes
	    blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2, new Vec2(ROBOT_WIDTH - (ROBOT_WIDTH/2), ROBOT_HEIGTH - (ROBOT_HEIGTH/2)), (float)direction);
	}

	public void rotateRight() {
		//TODO get direction from this.body.getVelocity() ???
		direction = (direction + 90)%360;
		Vec2 vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(direction))*INITIAL_SPEED;
		vec.y = (float)Math.sin(Math.toRadians(direction))*INITIAL_SPEED;
		this.body.setLinearVelocity(vec);
		//TODO need to check dimension of the PolygonShape, I think something is wrong collision are a little bit weird sometimes
	    blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2, new Vec2(ROBOT_WIDTH - (ROBOT_WIDTH/2), ROBOT_HEIGTH - (ROBOT_HEIGTH/2)), (float)direction);
	}
	
	public void jumpTo(Vec2 vec) {
		if(vec == null) {
			this.body.applyForce(new Vec2(0,0), this.getBody().getLocalCenter());
			return;
		}
		System.out.println("Jump");
		Vec2 p1 = this.getBody().getPosition();
		Vec2 p2 = vec;
		double newDirection = Math.atan((p2.y-p1.y) / (p2.x-p1.x));
		newDirection = Math.toDegrees(newDirection);
		if(newDirection<0) newDirection = 360+newDirection;
		System.out.println(newDirection);
		vec = new Vec2();
		vec.x = (float)Math.cos(Math.toRadians(newDirection))*INITIAL_SPEED*100;
		vec.y = (float)Math.sin(Math.toRadians(newDirection))*INITIAL_SPEED*100;
		//Remove current speed
		this.body.setLinearVelocity(new Vec2(0,0));
		//Apply directional force
		this.body.applyForce(vec, this.getBody().getLocalCenter());
		//TODO refresh shape like rotate
	    //blockShape.setAsBox(ROBOT_WIDTH/2, ROBOT_HEIGTH/2, new Vec2(ROBOT_WIDTH - (ROBOT_WIDTH/2), ROBOT_HEIGTH - (ROBOT_HEIGTH/2)), (float)direction);
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
