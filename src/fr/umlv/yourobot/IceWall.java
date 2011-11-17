package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class IceWall extends Wall {
	
	private final String imagePath = "IceWall.jpg";

	
	public IceWall(Vec2 position) {
		//Null is test by super
		super(position);
	}

	@Override
	public int attackWithIce() {
		int life = getLife() - 100;
		setLife(life);
		return life;
	}

	@Override
	public int attackWithStone() {
		int life = getLife() - 50;
		setLife(life);
		return life;
	}

	@Override
	public int attackWithWood() {
		int life = getLife() - 50;
		setLife(life);
		return life;
	}

	@Override
	String getImagePath() {
		return this.imagePath;
	}
}
