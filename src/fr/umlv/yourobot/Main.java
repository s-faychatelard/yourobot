package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;

import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;

public class Main {
	public static void main(String[] args) {
		final int WIDTH = 800;
		final int HEIGHT = 600;

		Application.run("You robot", WIDTH, HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final PhysicsWorld world = new PhysicsWorld();
				final Robot robot = (Robot)world.addElement(new Robot(new Vec2(400, 300)));
				final Robot robot2 = (Robot)world.addElement(new Robot(new Vec2(300, 300)));
				for(;;) {
					final KeyboardEvent event = context.pollKeyboard();
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							if(event != null) {
								switch(event.getKey()) {
									case UP:
										robot.translate(new Vec2(0,-10000));
										break;
									case DOWN:
										robot.translate(new Vec2(0,10000));
										break;
									case LEFT:
										robot.translate(new Vec2(-10000,0));
										break;
									case RIGHT:
										robot.translate(new Vec2(10000,0));
										break;
									case Z:
										robot2.translate(new Vec2(0,-10000));
										break;
									case S:
										robot2.translate(new Vec2(0,10000));
										break;
									case Q:
										robot2.translate(new Vec2(-10000,0));
										break;
									case D:
										robot2.translate(new Vec2(10000,0));
										break;
								}
							}
							graphics.setBackground(Color.WHITE);
							graphics.setColor(Color.WHITE);
							graphics.fillRect(0,0,800,600);
							world.render(graphics);
						}
					});
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