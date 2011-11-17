package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class FakeRobot extends Robot {
	
	private final String imagePath = "fakeRobot.png";

	public FakeRobot(Vec2 position) {
		super(position);
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}
}
