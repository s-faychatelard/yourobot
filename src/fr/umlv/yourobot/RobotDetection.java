package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class RobotDetection implements Runnable {
	private RayCastCallbackRobotIA raycastCallback;
	private RobotIA robot;
	private Vec2 p1;
	private Vec2 p2;
	private static double diagonal = -1;
	
	public RobotDetection(RobotIA robot) {
		this.robot = robot;
		raycastCallback = new RayCastCallbackRobotIA();
		raycastCallback.init();
		if(diagonal == -1)
			diagonal = Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;
		System.out.println(diagonal);
	}
	
	public boolean detect(RobotPlayer robot) {
		p1 = new Vec2(this.robot.getBody().getPosition());
        p2 = new Vec2(robot.getBody().getPosition().x, robot.getBody().getPosition().y);
        int y = (int)(p2.y - p1.y);
        int x = (int)(p2.x - p1.x);
        int res = (int) Math.sqrt(x*x + y*y);
        if(res <= diagonal) return true;
        return false;
	}
	
	@Override
	public void run() {
		while(true) {
			int i=0;
			for(RobotPlayer rp : this.robot.getRobotsDetected()) {
				if(detect(rp)) {
					System.out.println("Detect robot " + i);
				}
				i++;
			}
	        try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
