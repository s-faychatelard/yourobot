package fr.umlv.yourobot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.*;

public class RobotIA extends Robot {
	private static double diagonal = -1;
	private ArrayList<RobotPlayer> robotsDetection;
	private RayCastCallbackRobotIA raycastCallback;
	private Vec2 point1;
	private Vec2 point2;

	public RobotIA(Vec2 position) {
		super(position);
		robotsDetection = new ArrayList<>();
		raycastCallback = new RayCastCallbackRobotIA();
		raycastCallback.init();
		if(diagonal == -1)
			diagonal = Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;
	}
	
	public void start() {
		final Random rand = new Random();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					int val = rand.nextInt(45);
					if(rand.nextBoolean()) {
						for(int i=0; i<val; i++) {
							rotateLeft();
							try {
								Thread.sleep(rand.nextInt(500));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					else {
						for(int i=0; i<val; i++) {
							rotateRight();
							try {
								Thread.sleep(rand.nextInt(500));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(rand.nextInt(5000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					point1 = new Vec2(getBody().getPosition());
			        point2 = new Vec2((int)getBody().getPosition().x,(int)(getBody().getPosition().y+diagonal));
			    //    PhysicsWorld.addRaycast(raycastCallback, point1, point2);
				}
			}
		}).start();
	}

	public void detect(RobotPlayer robot) {
		Objects.requireNonNull(robot);
		robotsDetection.add(robot);
	}
	
	public void removeFormDetection(RobotPlayer robot) {
		robotsDetection.remove(robot);
	}
}
