package fr.umlv.yourobot;

import java.awt.Image;
import java.awt.Toolkit;

import org.jbox2d.common.Vec2;

public class RobotPlayer extends Robot {

	public RobotPlayer(Vec2 position) {
		super(position);
	}
	
	@Override
	public Image getImage() {
		if(image==null)
			image = Toolkit.getDefaultToolkit().getImage("robot.png");
	
		return this.image;
	}
	
	@Override
	public void setImage(Image img) {
		this.image = img;
	}

}
