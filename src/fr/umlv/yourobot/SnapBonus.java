package fr.umlv.yourobot;

import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

public class SnapBonus extends Bonus {
	
	private final String imagePath = "bomberBonus.png";

	public SnapBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public void execute(final RobotPlayer robot) {
		System.out.println("SNAP");
		return;
		//TODO queryCallback
		/*final Element element=null;
		Objects.requireNonNull(robot);
		Objects.requireNonNull(element);
		new Thread(new Runnable() {
			@Override
			public void run() {
				DistanceJointDef djd = new DistanceJointDef();
				BodyType oldType = element.getBody().getType();
				element.getBody().setType(BodyType.DYNAMIC);
				djd.initialize(robot.getBody(), element.getBody(), new Vec2(100,100), new Vec2(100,100));
				Joint j = PhysicsWorld.addJoint(djd);
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PhysicsWorld.deleteJoint(j);
				element.getBody().setType(oldType);
			}
		}).start();*/
	}
}
