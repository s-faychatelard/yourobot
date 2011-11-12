package fr.umlv.yourobot;

import java.util.Random;

import org.jbox2d.common.Vec2;

public class RobotIA extends Robot {

	public RobotIA(Vec2 position) {
		super(position);
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
						}
					}
					else {
						for(int i=0; i<val; i++) {
							rotateRight();
						}
					}
					try {
						Thread.sleep(rand.nextInt(5000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
