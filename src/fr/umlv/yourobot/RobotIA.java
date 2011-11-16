package fr.umlv.yourobot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;

public class RobotIA extends Robot {
	private ArrayList<RobotPlayer> robotsDetection;
	private Vec2 enemyPosition;

	public RobotIA(Vec2 position) {
		super(position);
		robotsDetection = new ArrayList<>();
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
						//int val = rand.nextInt(45);
						if(rand.nextInt(3)==2) {
							if(rand.nextBoolean()) {
								/*for(int i=0; i<val; i++) {
								rotateLeft();
								try {
									Thread.sleep(rand.nextInt(3000));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}*/
								rotateLeft();
							}
							else {
								/*for(int i=0; i<val; i++) {
								rotateRight();
								try {
									Thread.sleep(rand.nextInt(3000));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}*/
								rotateRight();
							}
						}
						try {
							Thread.sleep(rand.nextInt(1000));
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
		this.enemyPosition = point;
	}

	public ArrayList<RobotPlayer> getRobotsDetected() {
		return this.robotsDetection;
	}

	public void detect(RobotPlayer robot) {
		Objects.requireNonNull(robot);
		robotsDetection.add(robot);
	}

	public void removeFormDetection(RobotPlayer robot) {
		robotsDetection.remove(robot);
	}
}
