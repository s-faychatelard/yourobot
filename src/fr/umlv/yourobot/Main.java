package fr.umlv.yourobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import fr.umlv.zen.Application;
import fr.umlv.zen.ApplicationCode;
import fr.umlv.zen.ApplicationContext;
import fr.umlv.zen.ApplicationRenderCode;
import fr.umlv.zen.KeyboardEvent;

public class Main {
	public static void main(String[] args) {
		final int WIDTH = 800;
		final int HEIGHT = 600;
		final int SIZE = 30;

		Application.run("Keyboard", WIDTH, HEIGHT, new ApplicationCode() {
			@Override
			public void run(final ApplicationContext context) {
				final Random random = new Random(0);

				float x=10;
				float y=10;
				x = random.nextInt(WIDTH);
				y = random.nextInt(HEIGHT);

				final Ellipse2D ellipse = new Ellipse2D.Float(x - SIZE/2, y - SIZE/2, SIZE, SIZE);
				for(;;) {
					final KeyboardEvent event = context.waitKeyboard();
					if (event == null) {
						return;
					}
					context.render(new ApplicationRenderCode() {
						@Override
						public void render(Graphics2D graphics) {
							double x = ellipse.getX();
							double y = ellipse.getY();
							graphics.setBackground(Color.WHITE);
							graphics.setColor(Color.WHITE);
							graphics.clearRect((int)x, (int)y, (int)ellipse.getWidth(), (int)ellipse.getHeight());
							switch(event.getKey()) {
							case UP:
								y-=10;
								break;
							case DOWN:
								y+=10;
								break;
							case LEFT:
								x-=10;
								break;
							case RIGHT:
								x+=10;
								break;
							default:
							}
							ellipse.setFrame(x, y, ellipse.getWidth(), ellipse.getHeight());
							graphics.setColor(Color.BLACK);
							graphics.fill(ellipse);
							System.out.println(event.toString());
						}
					});
				}
			}
		});
	}
}