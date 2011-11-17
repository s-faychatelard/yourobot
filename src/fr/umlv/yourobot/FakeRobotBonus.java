package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class FakeRobotBonus extends Bonus {
	
	private final String imagePath = "fakeRobotBonus.png";

	public FakeRobotBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
}
