package fr.umlv.yourobot;

import java.awt.Image;
import java.awt.Toolkit;
import org.jbox2d.common.Vec2;

public class SnapBonus extends Bonus {
	
	private Image image;

	public SnapBonus(Vec2 position) {
		super(position);
	}
	
	@Override
	public Image getImage() {
		if (image == null)
			setImage(Toolkit.getDefaultToolkit().getImage("bomberBonus.png"));

		return this.image;
	}
	
	@Override
	public void setImage(Image img) {
		this.image = img;
	}
}
