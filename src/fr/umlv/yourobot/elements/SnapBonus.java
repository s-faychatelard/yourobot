package fr.umlv.yourobot.elements;

import java.util.LinkedList;
import java.util.Objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

import fr.umlv.yourobot.physics.World;

public class SnapBonus extends Bonus {

	private static final String imagePath = "snapBonus.png";

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
		Objects.requireNonNull(robot);
		LinkedList<Element> elements = World.getAllElement();
		for(final Element element : elements) {
			if(element instanceof EndPoint || element instanceof StartPoint || element instanceof RobotIA || element instanceof Bonus || element instanceof FakeRobot) continue;
			//Get the distance from the robot to the element
			int x = (int)robot.getBody().getPosition().x - (int)element.getBody().getPosition().x;
			int y = (int)robot.getBody().getPosition().y - (int)element.getBody().getPosition().y;
			double distance = Math.sqrt(x*x + y*y);
			if(distance>QUARTER_DIAGONAL/2) continue;
			new Thread(new Runnable() {
				@Override
				public void run() {
					DistanceJointDef djd = new DistanceJointDef();
					BodyType oldType = element.getBody().getType();
					element.getBody().setType(BodyType.DYNAMIC);
					djd.initialize(robot.getBody(), element.getBody(), new Vec2(100,100), new Vec2(100,100));
					Joint j = World.addJoint(djd);
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					World.deleteJoint(j);
					element.getBody().setType(oldType);
				}
			}).start();
		}
	}
}
