package fr.umlv.yourobot.elements;

import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.Main;
import fr.umlv.yourobot.physics.RayCastCallbackRobotIA;
import fr.umlv.yourobot.physics.World;

public class RobotIA extends Robot {
	private static final int TIME_DETECT = 4000;
	private Date date;
	private long lastDetectionTime=-1;
	private String imagePath = "robot.png";
	private final static RayCastCallbackRobotIA callback = new RayCastCallbackRobotIA();
	private final static double diagonal= Math.sqrt((Main.WIDTH*Main.WIDTH) + (Main.HEIGHT*Main.HEIGHT)) / 4;

	public RobotIA(Vec2 position) {
		//Null is test by super
		super(position);
	}

	@Override
	public String getImagePath() {
		return this.imagePath;
	}

	public void update() {
		Random rand = new Random();
		if(lastDetectionTime!=-1) {
			date = new Date();
			long time = date.getTime();
			if(time>lastDetectionTime+TIME_DETECT) lastDetectionTime=-1;
			return;
		}
		//Start detection
		if(this.detect()) {
			date = new Date();
			lastDetectionTime = date.getTime();
		} else {
			int val = rand.nextInt(45);
			if(rand.nextInt(10) == 1) {
				if(rand.nextBoolean()) {
					rotate(-val);
				}
				else {
					rotate(val);
				}
				go();
			}
		}
	}
	
	private void go() {
		Vec2 vec = new Vec2();
		vec.x = (float) Math.cos(Math.toRadians(this.getDirection())) * Robot.INITIAL_SPEED*10;
		vec.y = (float) Math.sin(Math.toRadians(this.getDirection())) * Robot.INITIAL_SPEED*10;
		this.getBody().setLinearVelocity(vec);
	}

	private boolean detect(Robot robot) {
		Objects.requireNonNull(robot);
		if(robot.getLife()<=0) return false;
		Vec2 p1 = new Vec2(this.getBody().getPosition());
		Vec2 p2 = new Vec2(robot.getBody().getPosition().x, robot.getBody().getPosition().y);
		int y = (int)(p2.y - p1.y);
		int x = (int)(p2.x - p1.x);
		int res = (int) Math.sqrt(x*x + y*y);
		if(res <= diagonal) return true;
		return false;
	}

	public boolean detect() {
		LinkedBlockingDeque<Robot> robots = World.getDetectableRobot();
		for(Robot robot : robots) {
			if(detect(robot)) {
				Vec2 vec1 = this.getBody().getPosition();
				Vec2 vec2 = robot.getBody().getPosition();
				callback.init();
				this.getBody().getWorld().raycast(callback, vec1, vec2);
				if(callback.getCount()<=1) {
					this.jumpToDetectedRobot(robot.getBody().getPosition());
					return true;
				}
			}
		}
		return false;
	}
}
