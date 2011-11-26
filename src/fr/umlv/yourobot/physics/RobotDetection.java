package fr.umlv.yourobot.physics;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.elements.Robot;
import fr.umlv.yourobot.elements.RobotIA;

public class RobotDetection implements Runnable {
	private final static RayCastCallbackRobotIA callback = new RayCastCallbackRobotIA();
	private final RobotIA robot;
	private final static double diagonal= Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;
	private final static Lock lock = new ReentrantLock();

	public RobotDetection(RobotIA robot) {
		this.robot = Objects.requireNonNull(robot);
	}

	public boolean detect(Robot robot) {
		Objects.requireNonNull(robot);
		if(robot.getLife()<=0) return false;
		Vec2 p1 = new Vec2(this.robot.getBody().getPosition());
		Vec2 p2 = new Vec2(robot.getBody().getPosition().x, robot.getBody().getPosition().y);
		int y = (int)(p2.y - p1.y);
		int x = (int)(p2.x - p1.x);
		int res = (int) Math.sqrt(x*x + y*y);
		if(res <= diagonal) return true;
		return false;
	}

	@Override
	public void run() {
		while(true) {
			Vec2 res = null;
			for(Robot robot : World.getDetectableRobot()) {
				if(detect(robot)) {
					//Not necessary to wait the lock, try again later
					if (lock.tryLock()) {
						try {
							Vec2 vec1 = this.robot.getBody().getPosition();
							Vec2 vec2 = robot.getBody().getPosition();
							callback.init();
							this.robot.getBody().getWorld().raycast(callback, vec1, vec2);
							if(callback.getCount()<=1) {
								res = robot.getBody().getPosition();
								break;
							}
						} finally {
							lock.unlock();
						}
					}
				}
			}
			this.robot.goTo(res);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
