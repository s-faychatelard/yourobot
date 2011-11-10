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
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static void main(String[] args) {
		Application.run("You robot", Main.WIDTH, Main.HEIGHT, new ApplicationCode() {
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