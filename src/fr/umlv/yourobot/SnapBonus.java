package fr.umlv.yourobot;

import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class SnapBonus extends Bonus {
	
	private final String imagePath = "bomberBonus.png";

	public SnapBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}
	
	void snap(final RobotPlayer robot, final Element element) {
		Objects.requireNonNull(robot);
		Objects.requireNonNull(element);
		new Thread(new Runnable() {
			@Override
			public void run() {
				WeldJointDef wj = new WeldJointDef();
				Vec2 p1 = robot.getBody().getPosition();
				Vec2 p2 = element.getBody().getPosition();
				Vec2 p = new Vec2(p1.x - p2.x, p1.y - p2.y);
				wj.initialize(robot.getBody(), element.getBody(), p);
				Joint j = PhysicsWorld.addJoint(wj);
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PhysicsWorld.deleteJoint(j);
			}
		}).start();
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
}
