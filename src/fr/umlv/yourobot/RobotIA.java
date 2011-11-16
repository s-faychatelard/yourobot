package fr.umlv.yourobot;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import org.jbox2d.common.Vec2;

public class RobotIA extends Robot {
	//Need to be concurrent safe because RobotDetection thread have an access to it
	private ArrayBlockingQueue<RobotPlayer> robotsDetection;
	private Vec2 enemyPosition;

	public RobotIA(Vec2 position) {
		super(position);
		robotsDetection = new ArrayBlockingQueue<RobotPlayer>(5);
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
						if(rand.nextBoolean()) {
							rotate(-val);
						}
						else {
							rotate(val);
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
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//Let's try to the player to go somewhere else
						jumpTo(new Vec2(rand.nextInt(500), rand.nextInt(500)));
						try {
							Thread.sleep(2000);
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
		this.enemyPosition = point;
	}

	public ArrayBlockingQueue<RobotPlayer> getRobotsDetected() {
		//If a robot deaded, remove it from the detection list
		for(RobotPlayer rp : this.robotsDetection)
			if(rp.getLife()<=0) this.robotsDetection.remove(rp);
		return this.robotsDetection;
	}

	public void detect(RobotPlayer robot) {
		Objects.requireNonNull(robot);
		robotsDetection.add(robot);
	}
}
