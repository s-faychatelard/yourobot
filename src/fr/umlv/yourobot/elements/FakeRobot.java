package fr.umlv.yourobot.elements;

import org.jbox2d.common.Vec2;

public class FakeRobot extends Robot {
	
	private final String imagePath = "fakeRobot.png";

	public FakeRobot(Vec2 position) {
		//Null is test by super
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
}
