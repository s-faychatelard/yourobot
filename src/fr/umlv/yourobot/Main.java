package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.LineJointDef;

import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;

public class Main {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	

	public static void main(String[] args) {
		/**
		 * Essai de modification pour faire un commit
		 */
		System.out.println("tutututututu");
		Application.run("You robot", Main.WIDTH, Main.HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final PhysicsWorld world = new PhysicsWorld();
				final Robot robot = (Robot)world.addElement(new Robot(new Vec2(300, 320)));
				final Robot robot2 = (Robot)world.addElement(new Robot(new Vec2(300, 300)));
				/*LineJointDef jd = new LineJointDef();
				Vec2 axis = new Vec2(1.f, 1.f);
				axis.normalize();
				jd.initialize(robot.getBody(), robot2.getBody(), new Vec2(.0f, .0f), axis);
				/*jd.enableMotor = false;
				jd.lowerTranslation = .0f;
				jd.upperTranslation = .0f;*
				jd.enableLimit = true;
				world.world.createJoint(jd);*/
				final RobotIA robot3 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 300)));
				final RobotIA robot4 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 400)));
				final RobotIA robot5 = (RobotIA)world.addElement(new RobotIA(new Vec2(600, 500)));
				robot3.start();
				robot4.start();
				robot5.start();
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
							world.render(graphics);
						}
					});
					//Need wait to delete flashing
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
	}
}