package fr.umlv.yourobot;

import java.util.LinkedList;
import java.util.Queue;

import org.jbox2d.common.Vec2;

public class RobotPlayer extends Robot {
	
	private final String imagePath = "robot.png";
	private final Queue<Bonus> bonus;

	public RobotPlayer(Vec2 position) {
		//Null is test by super
		super(position);
		getFixtureDef().filter.categoryBits = 1;
		bonus = new LinkedList<>();
	}
	
	public void takeBonus(Bonus bonus) {
		this.bonus.add(bonus);
		bonus.setTaken();
	}
	
	public void useBonus() {
		if(bonus.size()<=0) return;
		bonus.poll().execute(this);
	}
	
	@Override
	public String getImagePath() {
		return this.imagePath;
	}
}
