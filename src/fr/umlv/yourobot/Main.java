package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import org.jbox2d.common.Vec2;

import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;

public class Main {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static Image ground;

	public static void main(String[] args) {
		Application.run("YRobot", Main.WIDTH, Main.HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final PhysicsWorld world = new PhysicsWorld();
				final RobotPlayer robot = (RobotPlayer)world.addElement(new RobotPlayer(new Vec2(50, 50)));
				final RobotPlayer robot2 = (RobotPlayer)world.addElement(new RobotPlayer(new Vec2(300, 300)));
				System.out.println("Robot " + robot.getBody());
				System.out.println("Robot2 " + robot2.getBody());
				System.out.println("------------------");
				final RobotIA robot3 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 300)));
				final RobotIA robot4 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 400)));
				final RobotIA robot5 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 500)));
				robot3.start();
				robot4.start();
				robot5.start();
				robot3.detect(robot);
				robot3.detect(robot2);
				robot4.detect(robot);
				robot4.detect(robot2);
				robot5.detect(robot);
				robot5.detect(robot2);	
				
				final FakeRobot fake = (FakeRobot)world.addElement(new FakeRobot(new Vec2(100, 100)));

				//Load background textures
				ground = Toolkit.getDefaultToolkit().getImage("ground.jpg");
				
				final SnapBonus sb = new SnapBonus(new Vec2(800,600));
				sb.snap(robot, fake);

				new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							world.updateWorld();
							try {
								Thread.sleep(30);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

				for(;;) {
					final KeyboardEvent event = context.pollKeyboard();
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							if(event != null) {
								switch(event.getKey()) {
								case UP:
									robot.impulse();
									break;
								case DOWN:
									robot.brake();
									break;
								case LEFT:
									robot.rotateLeft();
									break;
								case RIGHT:
									robot.rotateRight();
									break;
								case Z:
									robot2.impulse();
									break;
								case S:
									robot2.brake();
									break;
								case Q:
									robot2.rotateLeft();
									break;
								case D:
									robot2.rotateRight();
									break;
								}
							}
							graphics.drawImage(ground, 0, 0, Main.WIDTH, Main.HEIGHT, Color.WHITE, null);
							world.render(graphics);
							graphics.finalize();
							graphics.dispose();
						}
					});
					//Need wait to delete flashing
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
	}
}
