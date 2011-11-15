package fr.umlv.yourobot;

import java.awt.Image;
import java.awt.Toolkit;
import org.jbox2d.common.Vec2;

public class IceWall extends Wall {
	
	private Image image;

	public IceWall(Vec2 position) {
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
	public Image getImage() {
		if (image == null)
			setImage(Toolkit.getDefaultToolkit().getImage("iceWall.jpg"));

		return this.image;
	}
	
	@Override
	public void setImage(Image img) {
		this.image = img;
	}
}