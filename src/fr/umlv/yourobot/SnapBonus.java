package fr.umlv.yourobot;

import java.awt.Image;
import java.awt.Toolkit;
import org.jbox2d.common.Vec2;

public class SnapBonus extends Bonus {
	
	private Image image;

	public SnapBonus(Vec2 position) {
		super(position);
		if (image == null)
			setImage(Toolkit.getDefaultToolkit().getImage("bomberBonus.png"));
	}
	
	@Override
	public Image getImage() {


		return this.image;
	}
	
	@Override
	public void setImage(Image img) {
		this.image = img;
	}
}
