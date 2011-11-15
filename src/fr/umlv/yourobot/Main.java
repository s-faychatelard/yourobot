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
		Application.run("You robot", Main.WIDTH, Main.HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final PhysicsWorld world = new PhysicsWorld();
				final RobotPlayer robot = (RobotPlayer)world.addElement(new RobotPlayer(new Vec2(300, 390)));
				final RobotPlayer robot2 = (RobotPlayer)world.addElement(new RobotPlayer(new Vec2(300, 300)));
				System.out.println(robot.getBody());
				System.out.println(robot2.getBody());
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
				//Load background texture
				ground = Toolkit.getDefaultToolkit().getImage("ground.jpg");
				
				for(;;) {
					final KeyboardEvent event = context.pollKeyboard();
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							if(event != null) {
								switch(event.getKey()) {
								case LEFT:
									robot.rotateLeft();
									break;
								case RIGHT:
									robot.rotateRight();
									break;
								case Q:
									robot2.rotateLeft();
									break;
								case D:
									robot2.rotateRight();
									break;
								}
							}
							graphics.setBackground(Color.WHITE);
							graphics.setColor(Color.WHITE);
							graphics.fillRect(0,0,800,600);
							graphics.drawImage(ground, 0, 0, 800, 600, Color.WHITE, null);
							world.render(graphics);
							graphics.finalize();
							graphics.dispose();
						}
					});
					//Need wait to delete flashing
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
	}
}
