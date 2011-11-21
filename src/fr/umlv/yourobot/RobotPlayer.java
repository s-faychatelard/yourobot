package fr.umlv.yourobot;

import java.util.LinkedList;
import java.util.Queue;

import org.jbox2d.common.Vec2;

public class RobotPlayer extends Robot {
	
	private final String imagePath = "robot.png";
	private final Queue<Bonus> bonus;
	private final Object lock = new Object();

	public RobotPlayer(Vec2 position) {
		//Null is test by super
		super(position);
		getFixtureDef().filter.categoryBits = 1;
		bonus = new LinkedList<>();
	}
	
	public void takeBonus(Bonus bonus) {
		if(this.bonus.contains(bonus)) return;
		this.bonus.add(bonus);
		PhysicsWorld.removeBody(bonus);
		bonus.setTaken();
	}
	
	public void useBonus() {
		if(this.getLife()<=0) return;
		synchronized (lock) {
			if(bonus.size()<=0) return;
			Bonus b = bonus.poll();
			b.execute(this);
		}
	}
	
	@Override
	public String getImagePath() {
		return this.imagePath;
	}
}
