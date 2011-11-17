package fr.umlv.yourobot;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

import org.jbox2d.common.Vec2;

public class RobotIA extends Robot {
	//LinkedBlockingDeque offer concurrent list and addFirst, addLast methods
	//Need to be concurrent safe because RobotDetection thread have an access to it
	private LinkedBlockingDeque<RobotPlayer> robotsDetection;
	private Vec2 enemyPosition;
	private String imagePath = "robot.png";

	public RobotIA(Vec2 position) {
		//Null is test by super
		super(position);
		getFixtureDef().filter.categoryBits = 2;
		robotsDetection = new LinkedBlockingDeque<>(5);
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
							Thread.sleep(3000);
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

	public LinkedBlockingDeque<RobotPlayer> getRobotsDetected() {
		//If a robot deaded, remove it from the detection list
		for(RobotPlayer rp : this.robotsDetection)
			if(rp.getLife()<=0) this.robotsDetection.remove(rp);
		return this.robotsDetection;
	}

	public void detect(RobotPlayer robot) {
		robotsDetection.add(Objects.requireNonNull(robot));
	}
	
	/*public void detectLeurre() {
		robotsDetection.addFirst(e);
	}*/
}
