package fr.umlv.yourobot;

import org.jbox2d.common.Vec2;

public class FakeRobotBonus extends Bonus {
	
	private final String imagePath = "bomberBonus.png";

	public FakeRobotBonus(Vec2 position) {
		//Null is test by super
		super(position);
	}
	
	@Override
	public String getImagePath() {
		return imagePath;
	}

	@Override
	public void execute(final RobotPlayer robot) {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				FakeRobot fr = new FakeRobot(new Vec2(robot.getBody().getPosition().x, robot.getBody().getPosition().y));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				PhysicsWorld.addLeurre(fr);
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PhysicsWorld.removeLeurre(fr);
			}
		}).start();;
	}
}
