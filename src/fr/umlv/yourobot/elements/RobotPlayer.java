package fr.umlv.yourobot.elements;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jbox2d.common.Vec2;

import fr.umlv.yourobot.physics.World;

public class RobotPlayer extends Robot {

	private final String imagePath = "robot.png";
	private final LinkedBlockingQueue<Bonus> bonus;
	private final Lock lock = new ReentrantLock();

	public RobotPlayer(Vec2 position) {
		//Null is test by super
		super(position);
		bonus = new LinkedBlockingQueue<>(50);
	}

	public void takeBonus(Bonus bonus) {
		lock.lock();
		try {
			if(this.bonus.contains(bonus)) return;
			this.bonus.add(bonus);
			World.removeBody(bonus);
			bonus.setTaken();
		} finally {
			lock.unlock();
		}
	}

	public void useBonus() {
		if(this.getLife()<=0) return;
		System.out.println(bonus.size());
		lock.lock();
		try {
			if(bonus.size()<=0) return;
			Bonus b = bonus.poll();
			b.execute(this);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String getImagePath() {
		return this.imagePath;
	}
}
