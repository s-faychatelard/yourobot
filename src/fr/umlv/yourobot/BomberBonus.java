package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class BomberBonus extends Bonus {
	
	private final String imagePath = "bomberBonus.png";

	public BomberBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}
}
