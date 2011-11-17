package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class SnapBonus extends Bonus {
	
	private final String imagePath = "bomberBonus.png";

	public SnapBonus(Vec2 position) {
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}
}
