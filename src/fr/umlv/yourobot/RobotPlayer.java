package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class RobotPlayer extends Robot {
	
	private final String imagePath = "robot.png";

	public RobotPlayer(Vec2 position) {
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return this.imagePath;
	}
}
