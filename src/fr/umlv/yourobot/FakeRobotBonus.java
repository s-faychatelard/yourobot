package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class FakeRobotBonus extends Bonus {
	
	private final String imagePath = "fakeRobotBonus.png";

	public FakeRobotBonus(Vec2 position) {
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
}
