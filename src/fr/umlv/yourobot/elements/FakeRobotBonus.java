package fr.umlv.yourobot.elements;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.World;

public class FakeRobotBonus extends Bonus {
	
	private static final String imagePath = "fakeRobotBonus.png";
	private final int executionTime;
	private Date date;
	private long startTime;
	private FakeRobot currentRobot;

	public FakeRobotBonus(Vec2 position) {
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
		currentRobot = new FakeRobot(new Vec2(robot.getBody().getPosition().x+25, robot.getBody().getPosition().y+25));
		World.addLeurre(currentRobot);
	}
	
	@Override
	public Bonus update() {
		date = new Date();
		long time = date.getTime();
		if(time>startTime+executionTime) return this;
		World.removeLeurre(currentRobot);
		return null;
	}
}
