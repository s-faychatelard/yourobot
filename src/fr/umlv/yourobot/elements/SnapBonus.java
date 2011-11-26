package fr.umlv.yourobot.elements;

import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.Joint;

import fr.umlv.yourobot.physics.World;

public class SnapBonus extends Bonus {

	private static final String imagePath = "snapBonus.png";
	private final int executionTime;
	private Date date;
	private long startTime;
	private LinkedList<SnapElement> elements;
	
	private static class SnapElement {
		Element element;
		BodyType oldBodyType;
		Joint joint;
	}

	public SnapBonus(Vec2 position) {
		//Null is test by super
		super(position);
		Random rand = new Random();
		executionTime = rand.nextInt(4000)+2000;
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public int getExecutionTime() {
		return this.executionTime;
	}

	@Override
	public void execute(final RobotPlayer robot) {
		Objects.requireNonNull(robot);
		date = new Date();
		startTime = date.getTime();
		LinkedList<Element> elements = World.getAllElement();
		elements = new LinkedList<>();
		for(final Element element : elements) {
			if(element instanceof EndPoint || element instanceof StartPoint || element instanceof RobotIA || element instanceof Bonus || element instanceof FakeRobot) continue;
			//Get the distance from the robot to the element
			int x = (int)robot.getBody().getPosition().x - (int)element.getBody().getPosition().x;
			int y = (int)robot.getBody().getPosition().y - (int)element.getBody().getPosition().y;
			double distance = Math.sqrt(x*x + y*y);
			if(distance>QUARTER_DIAGONAL/2) continue;
			
			SnapElement snapElement = new SnapElement();
			snapElement.element = element;
			DistanceJointDef djd = new DistanceJointDef();
			snapElement.oldBodyType = element.getBody().getType();
			element.getBody().setType(BodyType.DYNAMIC);
			djd.initialize(robot.getBody(), element.getBody(), new Vec2(100,100), new Vec2(100,100));
			snapElement.joint = World.addJoint(djd);
			elements.add(element);
		}
	}
	
	@Override
	public Bonus update() {
		date = new Date();
		long time = date.getTime();
		if(time>startTime+executionTime) return this;
		for(SnapElement snapElement : elements) {
			World.deleteJoint(snapElement.joint);
			snapElement.element.getBody().setType(snapElement.oldBodyType);
		}
		return null;
	}
}
