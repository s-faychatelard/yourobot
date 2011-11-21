package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

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
				//Create world (walls, robots, players, start, finish)
				final PhysicsWorld world = new PhysicsWorld(2);
				//Load background textures
				ground = Toolkit.getDefaultToolkit().getImage("ground.jpg");

				//Start world step update
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

				//Let's start the game
				//TODO IG
				for(;;) {
					final KeyboardEvent event = context.pollKeyboard();
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							if(event != null) {
								world.setKey(event.getKey());
							}
							graphics.drawImage(ground, 0, 0, Main.WIDTH, Main.HEIGHT, Color.WHITE, null);
							world.render(graphics);
						}
					});
					//Need wait to delete flashing
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
	}
}
