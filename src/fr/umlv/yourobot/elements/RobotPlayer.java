package fr.umlv.yourobot.elements;

import java.util.concurrent.LinkedBlockingQueue;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.World;

public class RobotPlayer extends Robot {

	private static final String imagePath = "robot.png";
	private final LinkedBlockingQueue<Bonus> bonus;

	public RobotPlayer(Vec2 position) {
		//Null is test by super
		super(position);
		bonus = new LinkedBlockingQueue<>(50);
	}

	public void takeBonus(Bonus bonus) {
		if(this.bonus.contains(bonus)) return;
		World.removeBody(bonus);
		this.bonus.add(bonus);
		bonus.setTaken();
	}

	public void useBonus() {
		if(this.getLife()<=0) return;
		System.out.println(bonus.size());
		if(bonus.size()<=0) return;
		Bonus b = bonus.poll();
		b.execute(this);
	}

	@Override
	public String getImagePath() {
		return imagePath;
	}
}
