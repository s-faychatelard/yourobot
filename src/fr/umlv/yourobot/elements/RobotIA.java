package fr.umlv.yourobot.elements;

import java.util.Random;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.RobotDetection;

public class RobotIA extends Robot {
	private Vec2 enemyPosition;
	private String imagePath = "robot.png";

	public RobotIA(Vec2 position) {
		//Null is test by super
		super(position);
		getFixtureDef().filter.categoryBits = 2;
	}

	@Override
	public String getImagePath() {
		return this.imagePath;
	}
	
	public void go() {
		Vec2 vec = new Vec2();
		vec.x = (float) Math.cos(Math.toRadians(this.getDirection())) * Robot.INITIAL_SPEED;
		vec.y = (float) Math.sin(Math.toRadians(this.getDirection())) * Robot.INITIAL_SPEED;
		this.getBody().setLinearVelocity(vec);
	}

	public void start() {
		//Start detection
		new Thread(new RobotDetection(this)).start();

		//Start movement
		new Thread(new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				while(true) {
					if(enemyPosition==null) {
						int val = rand.nextInt(45);
						if(rand.nextInt(3) == 2) {
							if(rand.nextBoolean()) {
								rotate(-val);
							}
							else {
								rotate(val);
							}
							go();
						}
						try {
							Thread.sleep(rand.nextInt(500));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						jumpTo(enemyPosition);
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						goTo(null);
						jumpTo(null);
					}
				}
			}
		}).start();
	}

	public void goTo(Vec2 point) {
		//point can be null
		this.enemyPosition = point;
	}
}
